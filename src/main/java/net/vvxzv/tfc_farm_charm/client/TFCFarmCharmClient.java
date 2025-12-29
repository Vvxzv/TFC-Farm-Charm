package net.vvxzv.tfc_farm_charm.client;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.registry.TFCFCBlock;

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
}
