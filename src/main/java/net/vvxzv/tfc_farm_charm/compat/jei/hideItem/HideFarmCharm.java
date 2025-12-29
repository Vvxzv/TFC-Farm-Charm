package net.vvxzv.tfc_farm_charm.compat.jei.hideItem;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.utils.JEIUtil;

import java.util.List;

@JeiPlugin
public class HideFarmCharm implements IModPlugin {
    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(TFCFarmCharm.MODID, "hide_farm_and_charm_item");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime){
        JEIUtil util = new JEIUtil(jeiRuntime);
        util.removeItemStacks(List.of(
                ObjectRegistry.OAT_PANCAKE.get().asItem(),
                ObjectRegistry.FARMERS_BREAKFAST.get().asItem(),
                ObjectRegistry.BAKED_LAMB_HAM.get().asItem(),
                ObjectRegistry.POTATO_WITH_ROAST_MEAT.get().asItem(),
                ObjectRegistry.STUFFED_CHICKEN.get().asItem(),
                ObjectRegistry.STUFFED_RABBIT.get().asItem(),
                ObjectRegistry.FARMERS_BREAD.get().asItem(),
                ObjectRegistry.GRANDMOTHERS_STRAWBERRY_CAKE.get().asItem(),
                ObjectRegistry.FERTILIZED_FARM_BLOCK.get().asItem(),
                ObjectRegistry.ROASTED_CORN_BLOCK.get().asItem()
        ));
    }
}
