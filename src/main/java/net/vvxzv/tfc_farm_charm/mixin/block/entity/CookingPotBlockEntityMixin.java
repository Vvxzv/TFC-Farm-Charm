package net.vvxzv.tfc_farm_charm.mixin.block.entity;

import net.dries007.tfc.common.items.FluidContainerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.farm_and_charm.core.block.entity.CookingPotBlockEntity;
import net.satisfy.farm_and_charm.core.world.ImplementedInventory;
import net.vvxzv.tfc_farm_charm.common.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CookingPotBlockEntity.class)
public abstract class CookingPotBlockEntityMixin extends BlockEntity implements ImplementedInventory {

    @Unique
    private ItemStack stack = ItemStack.EMPTY;

    public CookingPotBlockEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "isBeingBurned", at = @At("RETURN"), cancellable = true, remap = false)
    private void isBeingBurned(CallbackInfoReturnable<Boolean> cir) {
        Level level = this.level;
        if (level != null) {
            cir.setReturnValue(Utils.isBeingBurned(level, this.getBlockPos()));
        }
    }

    @Redirect(
            method = "craft",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/satisfy/farm_and_charm/core/block/entity/CookingPotBlockEntity;getItem(I)Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 1
            )
    )
    private ItemStack getStack(CookingPotBlockEntity instance, int i) {
        this.stack = this.getItem(i);
        return this.stack;
    }

    @ModifyVariable(method = "craft", at = @At("STORE"), name = "remainderStack", remap = false)
    private ItemStack returnFluidContainer(ItemStack remainderStack) {
        if(remainderStack.isEmpty()) {
            if(this.stack.getItem() instanceof FluidContainerItem) {
                return new ItemStack(this.stack.getItem());
            }
        }
        return remainderStack;
    }
}
