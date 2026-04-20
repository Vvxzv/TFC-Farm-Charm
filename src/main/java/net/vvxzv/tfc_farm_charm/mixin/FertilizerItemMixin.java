package net.vvxzv.tfc_farm_charm.mixin;

import net.dries007.tfc.common.blockentities.IFarmland;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.farm_and_charm.core.item.FertilizerItem;
import net.vvxzv.tfc_farm_charm.common.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FertilizerItem.class)
public class FertilizerItemMixin {

    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private boolean removePotential(List<Object> instance, Object e) {
        return false;
    }

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/context/UseOnContext;getItemInHand()Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    private void applyFarmlandNutrients(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        boolean applied = false;
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            BlockPos min = pos.offset(-3, -3, -3);
            BlockPos max = pos.offset(3, 3, 3);
            if (serverLevel.hasChunksAt(min, max)) {
                for(BlockPos blockPos : BlockPos.betweenClosed(min, max)) {
                    BlockEntity be = serverLevel.getBlockEntity(blockPos);
                    if (be instanceof IFarmland farmland) {
                        int which = serverLevel.random.nextInt(3);
                        float nut = serverLevel.random.nextFloat() * 0.5F;
                        Utils.receiveNutrients(farmland, 1.0F, which == 0 ? nut : 0.0F, which == 1 ? nut : 0.0F, which == 2 ? nut : 0.0F);
                        applied = true;
                    }
                }
            }
            if (applied) {
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
            }
        }
    }
}
