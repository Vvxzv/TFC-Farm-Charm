package net.vvxzv.tfc_farm_charm.mixin.block.entity;

import net.dries007.tfc.common.items.FluidContainerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.bakery.core.block.entity.SmallCookingPotBlockEntity;
import net.satisfy.farm_and_charm.core.world.ImplementedInventory;
import net.vvxzv.tfc_farm_charm.common.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(SmallCookingPotBlockEntity.class)
public abstract class SmallCookingPotBlockEntityMixin extends BlockEntity implements ImplementedInventory {
    public SmallCookingPotBlockEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "isBeingBurned", at = @At("RETURN"), remap = false, cancellable = true)
    private void isBeingBurned(CallbackInfoReturnable<Boolean> cir) {
        Level level = this.level;
        if (level != null) {
            cir.setReturnValue(Utils.isBeingBurned(level, this.getBlockPos()));
        }
    }

    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;forEach(Ljava/util/function/Consumer;)V"), remap = false)
    private void returnFluidContainer(NonNullList<Ingredient> instance, Consumer<Ingredient> consumer) {
        instance.forEach(ingredient -> {
            for(int slot = 0; slot < 6; slot++) {
                ItemStack stack = this.getItem(slot);
                if (ingredient.test(stack)) {
                    Item item = stack.getItem();
                    ItemStack remainderStack = item.getCraftingRemainingItem(stack);
                    stack.shrink(1);
                    if (!remainderStack.isEmpty()) {
                        this.setItem(slot, remainderStack);
                    }
                    if(item instanceof FluidContainerItem) {
                        if(this.getItem(slot).isEmpty()) {
                            this.setItem(slot, new ItemStack(item));
                        }
                    }
                    break;
                }
            }

        });
    }
}
