package net.vvxzv.tfc_farm_charm.common.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.vvxzv.tfc_farm_charm.api.fluid.AbstractAnimateFluid;
import net.vvxzv.tfc_farm_charm.api.fluid.AbstractAnimateFluidInstance;

public class BeerFluid extends AbstractAnimateFluid {

    public BeerFluid(AbstractAnimateFluidInstance instance){
        super(instance);
    }

    public static void tick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource) {

    }

    public void animateTick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource) {
        tick(level, blockPos, fluidState, randomSource);
    }

    public static class Source extends AbstractAnimateFluid.Source<BeerInstance> {
        public Source(BeerInstance instance) {
            super(instance);
        }

        public void animateTick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource) {
            BeerFluid.tick(level, blockPos, fluidState, randomSource);
        }
    }

    public static class Flowing extends AbstractAnimateFluid.Flowing<BeerInstance> {
        public Flowing(BeerInstance instance) {
            super(instance);
        }

        public void animateTick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource) {
            BeerFluid.tick(level, blockPos, fluidState, randomSource);
        }
    }
}
