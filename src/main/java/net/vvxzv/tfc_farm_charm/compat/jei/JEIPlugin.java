package net.vvxzv.tfc_farm_charm.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.bakery.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.compat.jei.category.CookingPotCategory;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(TFCFarmCharm.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ObjectRegistry.SMALL_COOKING_POT.get().asItem().getDefaultInstance(), CookingPotCategory.COOKING_POT);
    }
}
