package net.vvxzv.tfc_farm_charm.mixin.block.entity;

import net.dries007.tfc.common.items.FluidContainerItem;
import net.minecraft.world.item.ItemStack;
import net.satisfy.farm_and_charm.core.block.entity.CraftingBowlBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingBowlBlockEntity.class)
public class CraftingBowlBlockEntityMixin {

    @Inject(method = "getRemainderItem", at = @At("RETURN"), cancellable = true, remap = false)
    private void returnFluidContainer(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if(stack.getItem() instanceof FluidContainerItem) {
            cir.setReturnValue(new ItemStack(stack.getItem()));
        }
    }
}
