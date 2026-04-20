package net.vvxzv.tfc_farm_charm;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vvxzv.tfc_farm_charm.common.loot.LootModifiers;
import net.vvxzv.tfc_farm_charm.common.registry.*;
import net.vvxzv.tfc_farm_charm.common.utils.FoodTraits;

@Mod(TFCFarmCharm.MODID)
public class TFCFarmCharm {
    public static final String MODID = "tfc_farm_charm";

    @SuppressWarnings("removal")
    public TFCFarmCharm() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Blocks.BLOCKS.register(modEventBus);
        Items.ITEMS.register(modEventBus);
        BlockEntities.BLOCK_ENTITIES.register(modEventBus);
        Fluids.FLUIDS.register(modEventBus);
        LootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        CreativeTAB.CREATIVE_MODE_TAB.register(modEventBus);

        modEventBus.addListener(CreativeTAB::creativeTab);
        modEventBus.addListener(this::setup);

        ForgeEventHandler.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(FoodTraits::registerFoodTraits);
    }
}
