package net.vvxzv.tfc_farm_charm.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.block.BagBlock;
import net.vvxzv.tfc_farm_charm.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.tfc_farm_charm.common.block.entity.FertilizedFarmlandBlockEntity;
import net.vvxzv.tfc_farm_charm.compat.bakery.BakeryCompat;
import net.vvxzv.tfc_farm_charm.compat.brewery.BreweryCompat;
import net.vvxzv.tfc_farm_charm.compat.farm_and_charm.FarmAndCharmCompat;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TFCFarmCharm.MODID);

    private static Block[] getDecayingFoodBlocks() {
        Stream<Block> blocks = Stream.of(BakeryCompat.FOOD_BLOCKS, FarmAndCharmCompat.FOOD_BLOCKS).flatMap(Stream::of);

        if(ModList.get().isLoaded("brewery")) {
            blocks = Stream.of(BakeryCompat.FOOD_BLOCKS, BreweryCompat.FOOD_BLOCKS, FarmAndCharmCompat.FOOD_BLOCKS).flatMap(Stream::of);
        }

        Stream<Block> crateBlocks = BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof BagBlock);

        return Stream.concat(blocks, crateBlocks).toArray(Block[]::new);
    }

    public static final Supplier<BlockEntityType<DecayingFoodBlockEntity>> DECAYING = BLOCK_ENTITIES.register(
            "decaying",
            () -> BlockEntityType.Builder.of(
                    DecayingFoodBlockEntity::new,
                    getDecayingFoodBlocks()
            ).build(null)
    );

    public static final Supplier<BlockEntityType<FertilizedFarmlandBlockEntity>> FARMLAND = BLOCK_ENTITIES.register(
            "farmland",
            () -> BlockEntityType.Builder.of(
                    FertilizedFarmlandBlockEntity::new,
                    Blocks.FERTILIZED_FARMLAND.get()
            ).build(null)
    );
}
