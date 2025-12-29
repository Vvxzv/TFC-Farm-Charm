package net.vvxzv.tfc_farm_charm.common.registry;

import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.satisfy.bakery.core.registry.ObjectRegistry;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.block.decay.*;

import java.util.function.Supplier;

public class TFCFCBlock {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TFCFarmCharm.MODID);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> Block){
        RegistryObject<T> blocks = BLOCKS.register(name, Block);
        registerBlockItem(name, blocks);
        return blocks;
    }

    private static <T extends Block> RegistryObject<T> registerNoItemBlock(String name, Supplier<T> Block) {
        return BLOCKS.register(name, Block);
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> Block){
        TFCFCItem.ITEMS.register(name, () -> new BlockItem(Block.get(), new Item.Properties()));
    }

    private static ExtendedProperties foodProperties(){
        return ExtendedProperties.of(MapColor.COLOR_ORANGE)
                .mapColor(MapColor.COLOR_GREEN)
                .strength(1.0F)
                .sound(SoundType.WOOD)
                .blockEntity(TFCBlockEntities.DECAYING)
                .serverTicks(DecayingBlockEntity::serverTick)
                .instrument(NoteBlockInstrument.DIDGERIDOO)
                .pushReaction(PushReaction.DESTROY);
    }

    private static ExtendedProperties jarProperties(){
        return ExtendedProperties.of(Blocks.GLASS)
                .blockEntity(TFCBlockEntities.DECAYING)
                .serverTicks(DecayingBlockEntity::serverTick)
                .pushReaction(PushReaction.DESTROY)
                .instabreak()
                .sound(SoundType.GLASS)
                .noOcclusion();
    }

    public static final RegistryObject<Block> BREAD_CRATE = registerBlock("bread_crate", () -> new DecayingBreadBasketBlock(foodProperties(), TFCFCBlock.BREAD_CRATE));

    public static final RegistryObject<Block> CHOCOLATE_BOX = registerBlock("chocolate_box", () -> new DecayingEatableBoxBlock(foodProperties(),TFCFCBlock.BREAD_CRATE));

    public static final RegistryObject<Block> STRAWBERRY_JAM = registerBlock("strawberry_jam", () -> new DecayingStackableBlock(jarProperties(),3, TFCFCBlock.STRAWBERRY_JAM));

    public static final RegistryObject<Block> SWEETBERRY_JAM = registerBlock("sweetberry_jam", () -> new DecayingStackableBlock(jarProperties(),3, TFCFCBlock.STRAWBERRY_JAM));

    public static final RegistryObject<Block> GLOWBERRY_JAM = registerBlock("glowberry_jam", () -> new DecayingStackableBlock(jarProperties(),3, TFCFCBlock.STRAWBERRY_JAM));

    public static final RegistryObject<Block> APPLE_JAM = registerBlock("apple_jam", () -> new DecayingStackableBlock(jarProperties(),3, TFCFCBlock.STRAWBERRY_JAM));

    public static final RegistryObject<Block> CHOCOLATE_JAM = registerBlock("chocolate_jam", () -> new DecayingStackableBlock(jarProperties(),3, TFCFCBlock.STRAWBERRY_JAM));

    public static final RegistryObject<Block> CRUSTY_BREAD = registerNoItemBlock("crusty_bread", () -> new DecayingStackableEatableBlock(foodProperties(), 3, TFCFCBlock.CRUSTY_BREAD));

    public static final RegistryObject<Block> BREAD = registerNoItemBlock("bread", () -> new DecayingStackableEatableBlock(foodProperties(), 3, TFCFCBlock.BREAD));

    public static final RegistryObject<Block> BAGUETTE = registerNoItemBlock("baguette", () -> new DecayingStackableEatableBlock(foodProperties(), 4, TFCFCBlock.BAGUETTE));

    public static final RegistryObject<Block> TOAST = registerNoItemBlock("toast", () -> new DecayingStackableEatableBlock(foodProperties(), 3, TFCFCBlock.TOAST));

    public static final RegistryObject<Block> BRAIDED_BREAD = registerNoItemBlock("braided_bread", () -> new DecayingStackableEatableBlock(foodProperties(), 3, TFCFCBlock.BRAIDED_BREAD));

    public static final RegistryObject<Block> BUN = registerNoItemBlock("bun", () -> new DecayingStackableEatableBlock(foodProperties(), 4, TFCFCBlock.BUN));

    public static final RegistryObject<Block> STRAWBERRY_CAKE = registerBlock("strawberry_cake", () -> new DecayingCakeBlock(foodProperties(), ObjectRegistry.STRAWBERRY_CAKE_SLICE, TFCFCBlock.STRAWBERRY_CAKE));

    public static final RegistryObject<Block> SWEETBERRY_CAKE = registerBlock("sweetberry_cake", () -> new DecayingCakeBlock(foodProperties(), ObjectRegistry.STRAWBERRY_CAKE_SLICE, TFCFCBlock.SWEETBERRY_CAKE));

    public static final RegistryObject<Block> CHOCOLATE_CAKE = registerBlock("chocolate_cake", () -> new DecayingCakeBlock(foodProperties(), ObjectRegistry.CHOCOLATE_CAKE_SLICE, TFCFCBlock.CHOCOLATE_CAKE));

    public static final RegistryObject<Block> CHOCOLATE_GATEAU = registerBlock("chocolate_gateau", () -> new DecayingCakeBlock(foodProperties(), ObjectRegistry.CHOCOLATE_GATEAU_SLICE, TFCFCBlock.CHOCOLATE_GATEAU));

    public static final RegistryObject<Block> BUNDT_CAKE = registerBlock("bundt_cake", () -> new DecayingCakeBlock(foodProperties(), ObjectRegistry.BUNDT_CAKE_SLICE, TFCFCBlock.BUNDT_CAKE));

    public static final RegistryObject<Block> LINZER_TART = registerBlock("linzer_tart", () -> new DecayingLinzerTartBlock(foodProperties(), ObjectRegistry.LINZER_TART_SLICE, TFCFCBlock.LINZER_TART));

    public static final RegistryObject<Block> APPLE_PIE = registerBlock("apple_pie", () -> new DecayingApplePieBlock(foodProperties(), ObjectRegistry.APPLE_PIE_SLICE, TFCFCBlock.APPLE_PIE));

    public static final RegistryObject<Block> GLOWBERRY_TART = registerBlock("glowberry_tart", () -> new DecayingGlowberryTartBlock(foodProperties(), ObjectRegistry.GLOWBERRY_PIE_SLICE, TFCFCBlock.GLOWBERRY_TART));

    public static final RegistryObject<Block> CHOCOLATE_TART = registerBlock("chocolate_tart", () -> new DecayingChocolateTart(foodProperties(), ObjectRegistry.CHOCOLATE_TART_SLICE, TFCFCBlock.CHOCOLATE_TART));

    public static final RegistryObject<Block> PUDDING = registerBlock("pudding", () -> new DecayingPuddingBlock(foodProperties(), ObjectRegistry.PUDDING_SLICE, TFCFCBlock.PUDDING));

    public static final RegistryObject<Block> WAFFLE = registerNoItemBlock("waffle", () -> new DecayingStackableEatableBlock(foodProperties(), 4, TFCFCBlock.WAFFLE));

    public static final RegistryObject<Block> PORK_KNUCKLE = registerNoItemBlock("pork_knuckle", () -> new DecayingFoodBlock(foodProperties(), 3, 4, TFCFCBlock.PORK_KNUCKLE));

    public static final RegistryObject<Block> FRIED_CHICKEN = registerNoItemBlock("fried_chicken", () -> new DecayingFoodBlock(foodProperties(), 3, 4, TFCFCBlock.FRIED_CHICKEN));

    public static final RegistryObject<Block> DUMPLINGS = registerNoItemBlock("dumplings", () -> new DecayingFoodBlock(foodProperties(), 4, 4, TFCFCBlock.DUMPLINGS));

    public static final RegistryObject<Block> HALF_CHICKEN = registerNoItemBlock("half_chicken", () -> new DecayingFoodBlock(foodProperties(), 4, 4, TFCFCBlock.HALF_CHICKEN));

    public static final RegistryObject<Block> MASHED_POTATOES = registerNoItemBlock("mashed_potatoes", () -> new DecayingFoodBlock(foodProperties(), 4, 4, TFCFCBlock.MASHED_POTATOES));

    public static final RegistryObject<Block> POTATO_SALAD = registerNoItemBlock("potato_salad", () -> new DecayingFoodBlock(foodProperties(), 4, 4, TFCFCBlock.POTATO_SALAD));

    public static final RegistryObject<Block> OAT_PANCAKE = registerNoItemBlock("oat_pancake", () -> new DecayingStackableEatableBlock(foodProperties(), 7, TFCFCBlock.OAT_PANCAKE));

    public static final RegistryObject<Block> FARMERS_BREAKFAST = registerNoItemBlock("farmers_breakfast", () -> new DecayingFoodBlock(foodProperties(),4,4, TFCFCBlock.FARMERS_BREAKFAST));

    public static final RegistryObject<Block> BAKED_LAMB_HAM = registerNoItemBlock("baked_lamb_ham", () -> new DecayingFoodBlock(foodProperties(),4,4, TFCFCBlock.BAKED_LAMB_HAM));

    public static final RegistryObject<Block> POTATO_WITH_ROAST_MEAT = registerNoItemBlock("potato_with_roast_meat", () -> new DecayingFoodBlock(foodProperties(),4,4, TFCFCBlock.POTATO_WITH_ROAST_MEAT));

    public static final RegistryObject<Block> STUFFED_CHICKEN = registerNoItemBlock("stuffed_chicken", () -> new DecayingFoodBlock(foodProperties(),4,4, TFCFCBlock.STUFFED_CHICKEN));

    public static final RegistryObject<Block> STUFFED_RABBIT = registerNoItemBlock("stuffed_rabbit", () -> new DecayingFoodBlock(foodProperties(),4,4, TFCFCBlock.STUFFED_RABBIT));

    public static final RegistryObject<Block> FARMERS_BREAD = registerNoItemBlock("farmers_bread", () -> new DecayingFoodBlock(foodProperties(),4,4, TFCFCBlock.FARMERS_BREAD));

    public static final RegistryObject<Block> GRANDMOTHERS_STRAWBERRY_CAKE = registerNoItemBlock("grandmothers_strawberry_cake", () -> new DecayingFoodBlock(foodProperties(),4,4, TFCFCBlock.GRANDMOTHERS_STRAWBERRY_CAKE));

    public static final RegistryObject<Block> ROASTED_CORN = registerNoItemBlock("roasted_corn", () -> new DecayingStackableEatableBlock(foodProperties(), 4, TFCFCBlock.ROASTED_CORN));

    public static final RegistryObject<Block> FERTILIZED_FARMLAND = registerBlock("fertilized_farmland", () -> new FarmlandBlock(ExtendedProperties.of(MapColor.DIRT).strength(1.3F).sound(SoundType.GRAVEL).isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).blockEntity(TFCBlockEntities.FARMLAND), net.satisfy.farm_and_charm.core.registry.ObjectRegistry.FERTILIZED_SOIL_BLOCK));
}
