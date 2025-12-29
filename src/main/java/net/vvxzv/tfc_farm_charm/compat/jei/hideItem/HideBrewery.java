package net.vvxzv.tfc_farm_charm.compat.jei.hideItem;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.utils.JEIUtil;

import java.util.List;

@JeiPlugin
public class HideBrewery implements IModPlugin {
    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(TFCFarmCharm.MODID, "hide_brewery_item");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime){
        JEIUtil util = new JEIUtil(jeiRuntime);
        util.removeItemStacks(List.of(
                ObjectRegistry.PORK_KNUCKLE.get().asItem(),
                ObjectRegistry.FRIED_CHICKEN.get().asItem(),
                ObjectRegistry.DUMPLINGS.get().asItem(),
                ObjectRegistry.HALF_CHICKEN.get().asItem(),
                ObjectRegistry.MASHED_POTATOES.get().asItem(),
                ObjectRegistry.POTATO_SALAD.get().asItem()
        ));
    }
}
