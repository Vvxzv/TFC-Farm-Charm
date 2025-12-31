package net.vvxzv.tfc_farm_charm.common.registry;

import net.dries007.tfc.common.fluids.*;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.fluid.Beers;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class TFCFCFluid {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, TFCFarmCharm.MODID);

    private static FluidType.Properties waterLike() {
        return FluidType.Properties.create().adjacentPathType(BlockPathTypes.WATER).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).canConvertToSource(true).canDrown(true).canExtinguish(true).canHydrate(false).canPushEntity(true).canSwim(true).supportsBoating(true);
    }

    private static <F extends FlowingFluid> FluidRegistryObject<F> register(String name, Consumer<ForgeFlowingFluid.Properties> builder, FluidType.Properties typeProperties, FluidTypeClientProperties clientProperties, Function<ForgeFlowingFluid.Properties, F> sourceFactory, Function<ForgeFlowingFluid.Properties, F> flowingFactory) {
        int index = name.lastIndexOf(47);
        String flowingName = index == -1 ? "flowing_" + name : name.substring(0, index) + "/flowing_" + name.substring(index + 1);
        return RegistrationHelpers.registerFluid(TFCFluids.FLUID_TYPES, FLUIDS, name, name, flowingName, builder, () -> new ExtendedFluidType(typeProperties, clientProperties), sourceFactory, flowingFactory);
    }

    public static final Map<Beers, FluidRegistryObject<ForgeFlowingFluid>> BEERS = Helpers.mapOfKeys(Beers.class, (fluid) -> register(fluid.getSerializedName(), (properties) -> properties.block(TFCFCBlock.BEER_FLUIDS.get(fluid)).bucket(TFCFCItem.BEER_FLUID_BUCKETS.get(fluid)), waterLike().descriptionId("fluid.tfc_farm_charm." + fluid.getSerializedName()).canConvertToSource(false), new FluidTypeClientProperties(fluid.getColor(), TFCFluids.WATER_STILL, TFCFluids.WATER_FLOW, TFCFluids.WATER_OVERLAY, null), MixingFluid.Source::new, MixingFluid.Flowing::new));
}
