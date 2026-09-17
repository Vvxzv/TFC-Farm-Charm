package net.vvxzv.tfc_farm_charm.mixin.block.entity;

import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.dries007.tfc.util.Fuel;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
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
import net.vvxzv.tfc_farm_charm.common.utils.IStove;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StoveBlockEntity.class)
public abstract class StoveBlockEntityMixin implements ImplementedInventory, IStove {

    @Shadow(remap = false)
    protected int burnTime;

    @Shadow(remap = false)
    protected int burnTimeTotal;

    @Unique
    private float temperature = 0f;

    @Unique
    private float maxTemperature = 0f;

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
            this.setLit();
            this.setChanged();
        }

        if(this.burnTime == 0) {
            world.setBlockAndUpdate(pos, state.setValue(StoveBlock.LIT, false));
            this.setChanged();
        }
    }

    @Inject(
            method = "tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/satisfy/farm_and_charm/core/block/entity/StoveBlockEntity;)V",
            at = @At("HEAD"),
            remap = false
    )
    private void heating(Level world, BlockPos pos, BlockState state, StoveBlockEntity blockEntity, CallbackInfo ci) {
        if (this.temperature != this.maxTemperature) {
            this.temperature = HeatCapability.adjustTempTowards(this.temperature, this.maxTemperature);
            this.setChanged();
        }

        boolean isStoveLit = state.getValue(StoveBlock.LIT);
        if(isStoveLit){
            BlockEntity above = world.getBlockEntity(pos.above());
            if (above != null) {
                above.getCapability(HeatCapability.BLOCK_CAPABILITY).ifPresent((cap) -> {
                    float currentTemp = cap.getTemperature();
                    cap.setTemperature(HeatCapability.adjustTempTowards(currentTemp, this.temperature));
                });
            }
        }

        if(this.burnTime <= 0) {
            this.maxTemperature = 0;
        }
    }

    @Override
    public boolean setLit() {
        Fuel fuel = Fuel.get(this.getItem(4));
        if(fuel != null) {
            this.burnTime = this.burnTimeTotal = fuel.getDuration();

            float temp = fuel.getTemperature();
            if(temp > 900) {
                this.maxTemperature = 900;
                this.burnTime = this.burnTimeTotal = (int) (fuel.getDuration() + (temp - 900) * 20);
            } else {
                this.maxTemperature = temp;
            }

            this.removeItem(4, 1);
            return true;
        }

        return false;
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void load(CompoundTag nbt, CallbackInfo ci) {
        this.temperature = nbt.getFloat("temperature");
        this.maxTemperature = nbt.getFloat("maxTemperature");
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void saveAdditional(CompoundTag nbt, CallbackInfo ci) {
        nbt.putFloat("temperature", this.temperature);
        nbt.putFloat("maxTemperature", this.maxTemperature);
    }

    @Override
    public float getTemperature() {
        return this.temperature;
    }
}
