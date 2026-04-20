package net.vvxzv.tfc_farm_charm.client;

import net.dries007.tfc.client.model.ContainedFluidModel;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.DynamicFluidContainerModel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.registry.Items;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = TFCFarmCharm.MODID, bus = Mod.EventBusSubscriber.Bus.MOD , value = Dist.CLIENT )
public class TFCFarmCharmClient {

    @SubscribeEvent
    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event) {
        event.register(new ContainedFluidModel.Colors(), Items.CUP.get());

        for(Fluid fluid : ForgeRegistries.FLUIDS.getValues()) {
            if (Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).getNamespace().equals(TFCFarmCharm.MODID)) {
                event.register(new DynamicFluidContainerModel.Colors(), fluid.getBucket());
            }
        }
    }
}
