package net.vvxzv.tfc_farm_charm.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.satisfy.farm_and_charm.core.item.CatFoodItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(CatFoodItem.class)
public class CatFoodItemMixin {
    /**
     * @author Vvxzv
     * @reason null
     */
    @Overwrite
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.farm_and_charm.animal_fed_to_cat").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.tfc_farm_charm.cat_effect").withStyle(ChatFormatting.BLUE));
    }
}
