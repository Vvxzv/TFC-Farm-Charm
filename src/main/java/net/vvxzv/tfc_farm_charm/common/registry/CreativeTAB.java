package net.vvxzv.tfc_farm_charm.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.registry.TabRegistry;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.utils.Utils;

public class CreativeTAB {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFCFarmCharm.MODID);

    public static final RegistryObject<CreativeModeTab> TFC_FARM_CHARM = CREATIVE_MODE_TAB.register(TFCFarmCharm.MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("tfc_farm_charm.tab.name"))
            .icon(() -> new ItemStack(Items.CUP.get()))
            .displayItems((parm, output) -> {
                Items.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
            })
            .build()
    );

    public static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        if(event.getTab() == TabRegistry.FARM_AND_CHARM_TAB.get()) {
            Utils.replaceTabItem(event, ObjectRegistry.FERTILIZED_FARM_BLOCK.get(), Blocks.FERTILIZED_FARMLAND.get());
            Utils.replaceTabItem(event, ObjectRegistry.LETTUCE_BAG.get(), Blocks.BAGS_BLOCK_MAP.get(Blocks.Bags.CABBAGE).get());
            Utils.replaceTabItem(event, ObjectRegistry.TOMATO_BAG.get(), Blocks.BAGS_BLOCK_MAP.get(Blocks.Bags.TOMATO).get());
            Utils.replaceTabItem(event, ObjectRegistry.CARROT_BAG.get(), Blocks.BAGS_BLOCK_MAP.get(Blocks.Bags.CARROT).get());
            Utils.replaceTabItem(event, ObjectRegistry.POTATO_BAG.get(), Blocks.BAGS_BLOCK_MAP.get(Blocks.Bags.POTATO).get());
            Utils.replaceTabItem(event, ObjectRegistry.ONION_BAG.get(), Blocks.BAGS_BLOCK_MAP.get(Blocks.Bags.ONION).get());
            Utils.replaceTabItem(event, ObjectRegistry.BEETROOT_BAG.get(), Blocks.BAGS_BLOCK_MAP.get(Blocks.Bags.BEET).get());
            Utils.replaceTabItem(event, ObjectRegistry.CORN_BAG.get(), Blocks.BAGS_BLOCK_MAP.get(Blocks.Bags.MAIZE).get());
            Utils.replaceTabItem(event, ObjectRegistry.STRAWBERRY_BAG.get(), Blocks.BAGS_BLOCK_MAP.get(Blocks.Bags.STRAWBERRY).get());
            Utils.replaceTabItem(event, ObjectRegistry.FLOUR_BAG.get(), Blocks.BAGS_BLOCK_MAP.get(Blocks.Bags.FLOUR).get());
        }
    }
}
