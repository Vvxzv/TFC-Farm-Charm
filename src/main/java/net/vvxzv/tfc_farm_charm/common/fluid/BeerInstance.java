package net.vvxzv.tfc_farm_charm.common.fluid;

import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.vvxzv.tfc_farm_charm.api.fluid.AbstractAnimateFluid;
import net.vvxzv.tfc_farm_charm.api.fluid.AbstractAnimateFluidInstance;

public class BeerInstance extends AbstractAnimateFluidInstance {
    public BeerInstance(String modId, String fluid, FluidType.Properties fluidTypeProperties, IClientFluidTypeExtensions renderProperties) {
        super(modId, fluid, fluidTypeProperties, renderProperties);
    }

    @Override
    public AbstractAnimateFluid.Source<?> setSource(AbstractAnimateFluidInstance instance) {
        return new BeerFluid.Source(this);
    }

    @Override
    public AbstractAnimateFluid.Flowing<?> setFlowing(AbstractAnimateFluidInstance instance) {
        return new BeerFluid.Flowing(this);
    }
}
