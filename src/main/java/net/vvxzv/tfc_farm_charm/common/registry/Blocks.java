package net.vvxzv.tfc_farm_charm.common.registry;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.block.BagBlock;
import net.vvxzv.tfc_farm_charm.common.block.FertilizedFarmlandBlock;
import net.vvxzv.tfc_farm_charm.common.fluid.Beers;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TFCFarmCharm.MODID);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> supplierBlock){
        RegistryObject<T> block = BLOCKS.register(name, supplierBlock);
        Items.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static <T extends Block> RegistryObject<T> registerFluidBlock(String name, Supplier<T> blockSupplier) {
        return RegistrationHelpers.registerBlock(BLOCKS, Items.ITEMS, name, blockSupplier, null);
    }

    public static final RegistryObject<Block> FERTILIZED_FARMLAND = registerBlock(
            "fertilized_farmland",
            () -> new FertilizedFarmlandBlock(
                    ExtendedProperties.of(MapColor.DIRT)
                            .strength(1.3F)
                            .sound(SoundType.GRAVEL)
                            .isViewBlocking(TFCBlocks::always)
                            .isSuffocating(TFCBlocks::always)
                            .blockEntity(BlockEntities.FARMLAND),
                    ObjectRegistry.FERTILIZED_SOIL_BLOCK
            )
    );

    public static final Map<Beers, RegistryObject<LiquidBlock>> BEER_FLUIDS = Helpers.mapOfKeys(Beers.class, (fluid) -> registerFluidBlock(fluid.getSerializedName(), () -> new LiquidBlock((Fluids.BEERS.get(fluid)).source(), BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WATER).noLootTable())));

    public static final Map<Bags, RegistryObject<Block>> BAGS_BLOCK_MAP = Helpers.mapOfKeys(
            Bags.class,
            (block) -> registerBlock(block.getName(), BagBlock::new)
    );

    public enum Bags {
        CABBAGE,
        TOMATO,
        CARROT,
        POTATO,
        ONION,
        BEET,
        MAIZE,
        STRAWBERRY,
        FLOUR;

        final String name;

        Bags() {
            this.name = this.name().toLowerCase(Locale.ROOT) + "_bag";
        }

        public String getName() {
            return name;
        }
    }
}
