package net.vvxzv.tfc_farm_charm.common.utils;

import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;

public class FoodTraits {
    public static void registerFoodTraits() {
    }

    private static FoodTrait register(String name, float decayModifier) {
        return FoodTrait.register(ResourceLocation.fromNamespaceAndPath(TFCFarmCharm.MODID, name), new FoodTrait(decayModifier, "tfc_farm_charm.tooltip.food_trait." + name));
    }

    public static final FoodTrait CELLAR_PRESERVED = register("cellar_preserved", 0.5F);

}
