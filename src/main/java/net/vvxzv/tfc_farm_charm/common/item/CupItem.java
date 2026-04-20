package net.vvxzv.tfc_farm_charm.common.item;

import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.items.JugItem;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Drinkable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CupItem extends JugItem {
    public CupItem(Properties properties, Supplier<Integer> capacity, TagKey<Fluid> whitelist) {
        super(properties, capacity, whitelist);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        IFluidHandler handler = stack.getCapability(Capabilities.FLUID_ITEM).resolve().orElse(null);
        if (handler != null) {
            FluidStack drained = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
            if (entity instanceof Player player) {
                Drinkable drinkable = Drinkable.get(drained.getFluid());
                if (drinkable != null && !level.isClientSide) {
                    drinkable.onDrink(player, drained.getAmount()/2);
                }
            }

            if (entity.getRandom().nextFloat() < TFCConfig.SERVER.jugBreakChance.get()) {
                stack.shrink(1);
                level.playSound(null, entity.blockPosition(), SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        return stack;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 16;
    }

    @Override
    protected @NotNull InteractionResultHolder<ItemStack> afterFillFailed(@NotNull IFluidHandler handler, @NotNull Level level, @NotNull Player player, @NotNull ItemStack stack, @NotNull InteractionHand hand) {
        return InteractionResultHolder.success(stack);
    }
}
