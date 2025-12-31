package net.vvxzv.tfc_farm_charm.client;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.DynamicFluidContainerModel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.registry.TFCFCBlock;
import net.vvxzv.tfc_farm_charm.common.registry.TFCFCItem;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = TFCFarmCharm.MODID, bus = Mod.EventBusSubscriber.Bus.MOD , value = Dist.CLIENT )
public class TFCFarmCharmClient {
    @SubscribeEvent
    public static void onClient(FMLClientSetupEvent event){
        RenderTypeRegistry.register(RenderType.cutout(), new Block[]{
                TFCFCBlock.SWEETBERRY_JAM.get(),
                TFCFCBlock.CHOCOLATE_JAM.get(),
                TFCFCBlock.STRAWBERRY_JAM.get(),
                TFCFCBlock.GLOWBERRY_JAM.get(),
                TFCFCBlock.APPLE_JAM.get(),
                TFCFCBlock.FARMERS_BREAKFAST.get(),
                TFCFCBlock.STUFFED_CHICKEN.get(),
                TFCFCBlock.STUFFED_RABBIT.get()
        });
    }

    @SubscribeEvent
    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event) {
        event.register(new ContainedFluidModel.Colors(), TFCFCItem.CUP.get());

        for(Fluid fluid : ForgeRegistries.FLUIDS.getValues()) {
            if (Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).getNamespace().equals(TFCFarmCharm.MODID)) {
                event.register(new DynamicFluidContainerModel.Colors(), fluid.getBucket());
            }
        }
    }
}
