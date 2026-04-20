package net.vvxzv.tfc_farm_charm.mixin.block.entity;

import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.dries007.tfc.util.Fuel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.farm_and_charm.core.block.StoveBlock;
import net.satisfy.farm_and_charm.core.block.entity.StoveBlockEntity;
import net.satisfy.farm_and_charm.core.recipe.StoveRecipe;
import net.satisfy.farm_and_charm.core.world.ImplementedInventory;
import net.vvxzv.tfc_farm_charm.Config;
import net.vvxzv.tfc_farm_charm.common.utils.IStoveLitAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StoveBlockEntity.class)
public abstract class StoveBlockEntityMixin implements ImplementedInventory, IStoveLitAccess {

    @Shadow(remap = false)
    protected int burnTime;

    @Shadow(remap = false)
    protected int burnTimeTotal;

    @Shadow
    public abstract void setChanged();

    @Redirect(
            method = "craft",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/satisfy/farm_and_charm/core/block/entity/StoveBlockEntity;getRemainderItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
            ),
            remap = false
    )
    private ItemStack returnFluidContainer(StoveBlockEntity instance, ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof FluidContainerItem) {
            return new ItemStack(item);
        }

        return item.getCraftingRemainingItem(stack);
    }

    @Redirect(
            method = "tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/satisfy/farm_and_charm/core/block/entity/StoveBlockEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/satisfy/farm_and_charm/core/block/entity/StoveBlockEntity;canCraft(Lnet/satisfy/farm_and_charm/core/recipe/StoveRecipe;Lnet/minecraft/core/RegistryAccess;)Z",
                    ordinal = 0
            ),
            remap = false
    )
    private boolean canCraft(StoveBlockEntity instance, StoveRecipe recipe, RegistryAccess access) {
        return false;
    }

    @Inject(
            method = "tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/satisfy/farm_and_charm/core/block/entity/StoveBlockEntity;)V",
            at = @At("TAIL"),
            remap = false
    )
    private void tickTail(Level world, BlockPos pos, BlockState state, StoveBlockEntity blockEntity, CallbackInfo ci) {
        if(this.burnTime == 1) {
            this.setLit(true);
            this.setChanged();
        }
        if(this.burnTime == 0) {
            world.setBlockAndUpdate(pos, state.setValue(StoveBlock.LIT, false));
            this.setChanged();
        }
    }

    @Unique
    private static float stoveTemperature(){
        return (float) Config.heatingTemperature;
    }

    @Inject(
            method = "tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/satisfy/farm_and_charm/core/block/entity/StoveBlockEntity;)V",
            at = @At("HEAD"),
            remap = false
    )
    private void heating(Level world, BlockPos pos, BlockState state, StoveBlockEntity blockEntity, CallbackInfo ci) {
        boolean isStoveLit = state.getValue(StoveBlock.LIT);
        if(isStoveLit){
            BlockEntity above = world.getBlockEntity(pos.above());
            if (above != null) {
                above.getCapability(HeatCapability.BLOCK_CAPABILITY).ifPresent((cap) -> {
                    float blockTemperature = cap.getTemperature();
                    if(blockTemperature < stoveTemperature()){
                        float setTemperature = blockTemperature + 2;
                        if(setTemperature > stoveTemperature()) setTemperature = stoveTemperature();
                        cap.setTemperatureIfWarmer(setTemperature);
                    }
                });
            }
        }
    }

    @Override
    public boolean setLit(boolean l) {
        Fuel fuel = Fuel.get(this.getItem(4));
        if(fuel != null) {
            this.burnTime = this.burnTimeTotal = fuel.getDuration();
            this.removeItem(4, 1);
            return true;
        }

        return false;
    }
}
