package net.vvxzv.tfc_farm_charm.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.farm_and_charm.core.block.PetBowlBlock;
import net.satisfy.farm_and_charm.core.block.entity.PetBowlBlockEntity;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PetBowlBlock.class)
public class PetBowlBlockMixin{

    @Redirect(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/satisfy/farm_and_charm/core/block/entity/PetBowlBlockEntity;setItem(ILnet/minecraft/world/item/ItemStack;)V")
    )
    private void RedirectSetItem(PetBowlBlockEntity instance, int current, ItemStack stack) {

    }

    @Redirect(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/satisfy/farm_and_charm/core/block/entity/PetBowlBlockEntity;onFed(Lnet/minecraft/world/item/ItemStack;)V"),
            remap = false
    )
    private void RedirectOnFed(PetBowlBlockEntity instance, ItemStack stack) {

    }

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
                    ordinal = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void setItemOnBowl(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir, ItemStack heldItem, BlockEntity blockEntity, PetBowlBlockEntity entity, GeneralUtil.FoodType type) {
        entity.setItem(0, new ItemStack(heldItem.getItem()));
        entity.onFed(heldItem);
    }
}
