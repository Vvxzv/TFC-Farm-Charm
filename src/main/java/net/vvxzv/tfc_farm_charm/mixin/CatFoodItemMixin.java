package net.vvxzv.tfc_farm_charm.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.satisfy.farm_and_charm.core.item.CatFoodItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CatFoodItem.class)
public class CatFoodItemMixin {

    @ModifyArg(
            method = "appendHoverText",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1)
    )
    private Object replaceText(Object e) {
        return Component.translatable("tooltip.tfc_farm_charm.cat_effect").withStyle(ChatFormatting.BLUE);
    }
}
