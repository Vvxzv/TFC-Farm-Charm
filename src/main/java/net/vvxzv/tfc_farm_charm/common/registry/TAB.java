package net.vvxzv.tfc_farm_charm.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;

import java.util.List;

public class TAB {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFCFarmCharm.MODID);

    public static final RegistryObject<CreativeModeTab> TFC_FARM_CHARM;

    static {
        TFC_FARM_CHARM = CREATIVE_MODE_TAB.register(TFCFarmCharm.MODID, () -> CreativeModeTab.builder()
                .title(Component.translatable("tfc_farm_charm.tab.name"))
                .icon(() -> new ItemStack(TFCFCItem.CUP.get()))
                .displayItems((parm, output) -> {
                    List<RegistryObject<Item>> modItems = List.of(
                            TFCFCItem.CUP,
                            TFCFCItem.UNFINISHED_APPLE_PIE,
                            TFCFCItem.UNFINISHED_BAGUETTE,
                            TFCFCItem.UNFINISHED_BRAIDED_BREAD,
                            TFCFCItem.UNFINISHED_BREAD,
                            TFCFCItem.UNFINISHED_BUN,
                            TFCFCItem.UNFINISHED_BUNDT_CAKE,
                            TFCFCItem.UNFINISHED_CHOCOLATE_TART,
                            TFCFCItem.UNFINISHED_CROISSANT,
                            TFCFCItem.UNFINISHED_CRUSTY_BREAD,
                            TFCFCItem.UNFINISHED_FARMERS_BREAD,
                            TFCFCItem.UNFINISHED_GINGERBREAD,
                            TFCFCItem.UNFINISHED_GLOWBERRY_TART,
                            TFCFCItem.UNFINISHED_GRANDMOTHERS_STRAWBERRY_CAKE,
                            TFCFCItem.UNFINISHED_LINZER_TART,
                            TFCFCItem.UNFINISHED_PRETZEL,
                            TFCFCItem.UNFINISHED_TOAST,
                            TFCFCItem.UNFINISHED_WAFFLE
                    );
                    modItems.forEach(item -> output.accept(item.get()));

                    List<RegistryObject<Block>> modBlockItem = List.of(
                            TFCFCBlock.CRUSTY_BREAD,
                            TFCFCBlock.BREAD,
                            TFCFCBlock.BAGUETTE,
                            TFCFCBlock.TOAST,
                            TFCFCBlock.BRAIDED_BREAD,
                            TFCFCBlock.BUN,
                            TFCFCBlock.WAFFLE,
                            TFCFCBlock.PORK_KNUCKLE,
                            TFCFCBlock.FRIED_CHICKEN,
                            TFCFCBlock.DUMPLINGS,
                            TFCFCBlock.HALF_CHICKEN,
                            TFCFCBlock.MASHED_POTATOES,
                            TFCFCBlock.POTATO_SALAD,
                            TFCFCBlock.OAT_PANCAKE,
                            TFCFCBlock.FARMERS_BREAKFAST,
                            TFCFCBlock.BAKED_LAMB_HAM,
                            TFCFCBlock.POTATO_WITH_ROAST_MEAT,
                            TFCFCBlock.STUFFED_CHICKEN,
                            TFCFCBlock.STUFFED_RABBIT,
                            TFCFCBlock.GRANDMOTHERS_STRAWBERRY_CAKE,
                            TFCFCBlock.FARMERS_BREAD,
                            TFCFCBlock.BREAD_CRATE,
                            TFCFCBlock.CHOCOLATE_BOX,
                            TFCFCBlock.STRAWBERRY_JAM,
                            TFCFCBlock.SWEETBERRY_JAM,
                            TFCFCBlock.GLOWBERRY_JAM,
                            TFCFCBlock.APPLE_JAM,
                            TFCFCBlock.CHOCOLATE_JAM,
                            TFCFCBlock.STRAWBERRY_CAKE,
                            TFCFCBlock.SWEETBERRY_CAKE,
                            TFCFCBlock.CHOCOLATE_CAKE,
                            TFCFCBlock.CHOCOLATE_GATEAU,
                            TFCFCBlock.BUNDT_CAKE,
                            TFCFCBlock.LINZER_TART,
                            TFCFCBlock.APPLE_PIE,
                            TFCFCBlock.GLOWBERRY_TART,
                            TFCFCBlock.CHOCOLATE_TART,
                            TFCFCBlock.PUDDING,
                            TFCFCBlock.ROASTED_CORN,
                            TFCFCBlock.FERTILIZED_FARMLAND
                    );
                    modBlockItem.forEach(item -> output.accept(item.get()));
                })
                .build()
        );
    }
}
