package net.vvxzv.tfc_farm_charm.compat.jei.hideItem;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.bakery.core.registry.ObjectRegistry;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.utils.JEIUtil;

import java.util.List;

@JeiPlugin
public class HideBakery implements IModPlugin {
    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(TFCFarmCharm.MODID, "hide_bakery_item");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime){
        JEIUtil util = new JEIUtil(jeiRuntime);
        util.removeItemStacks(List.of(
                ObjectRegistry.CRUSTY_BREAD.get().asItem(),
                ObjectRegistry.BREAD.get().asItem(),
                ObjectRegistry.BAGUETTE.get().asItem(),
                ObjectRegistry.TOAST.get().asItem(),
                ObjectRegistry.BRAIDED_BREAD.get().asItem(),
                ObjectRegistry.BUN.get().asItem(),
                ObjectRegistry.WAFFLE.get().asItem(),
                ObjectRegistry.BREAD_CRATE.get().asItem(),
                ObjectRegistry.CHOCOLATE_BOX.get().asItem(),
                ObjectRegistry.STRAWBERRY_JAM.get().asItem(),
                ObjectRegistry.SWEETBERRY_JAM.get().asItem(),
                ObjectRegistry.GLOWBERRY_JAM.get().asItem(),
                ObjectRegistry.APPLE_JAM.get().asItem(),
                ObjectRegistry.CHOCOLATE_JAM.get().asItem(),
                ObjectRegistry.STRAWBERRY_CAKE.get().asItem(),
                ObjectRegistry.SWEETBERRY_CAKE.get().asItem(),
                ObjectRegistry.CHOCOLATE_CAKE.get().asItem(),
                ObjectRegistry.CHOCOLATE_GATEAU.get().asItem(),
                ObjectRegistry.BUNDT_CAKE.get().asItem(),
                ObjectRegistry.LINZER_TART.get().asItem(),
                ObjectRegistry.APPLE_PIE.get().asItem(),
                ObjectRegistry.GLOWBERRY_TART.get().asItem(),
                ObjectRegistry.CHOCOLATE_TART.get().asItem(),
                ObjectRegistry.PUDDING.get().asItem()
        ));
    }
}
