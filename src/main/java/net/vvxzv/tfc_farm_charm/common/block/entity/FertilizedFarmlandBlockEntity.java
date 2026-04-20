package net.vvxzv.tfc_farm_charm.common.block.entity;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.util.Fertilizer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.tfc_farm_charm.Config;
import net.vvxzv.tfc_farm_charm.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

public class FertilizedFarmlandBlockEntity extends FarmlandBlockEntity{

    public FertilizedFarmlandBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.FARMLAND.get(), pos, state);
    }

    private static float getFertilizerTimesValue() {
        return (float) Config.fertilizerOnFertilizedFarmland;
    }

    @Override
    public void addNutrients(@NotNull Fertilizer fertilizer, float multiplier) {
        float richMultiplier = multiplier * getFertilizerTimesValue();
        super.addNutrients(fertilizer, richMultiplier);
    }
}
