package net.vvxzv.tfc_farm_charm.api.fluid;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;

import java.util.function.Consumer;

@SuppressWarnings("removal")
public abstract class AbstractAnimateFluidInstance {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, TFCFarmCharm.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, TFCFarmCharm.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFCFarmCharm.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TFCFarmCharm.MODID);

    private final RegistryObject<FluidType> fluidType;
    private final RegistryObject<Fluid> flowingFluid;
    private final RegistryObject<Fluid> sourceFluid;
    private final RegistryObject<Item> bucketFluid;
    private final RegistryObject<Block> blockFluid;
    private final String fluid;
    private final String modId;

    public AbstractAnimateFluidInstance(String modId, String fluid, FluidType.Properties fluidTypeProperties, IClientFluidTypeExtensions renderProperties) {
        this.modId = modId;
        this.fluid = fluid;
        ResourceLocation fluidRL = new ResourceLocation(modId, fluid);

        this.sourceFluid = FLUIDS.register(fluid, () -> this.setSource(this));
        this.flowingFluid = FLUIDS.register(fluid + "_flowing", () -> this.setFlowing(this));
        this.fluidType = FLUID_TYPES.register(fluid, () -> new FluidType(fluidTypeProperties) {

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(renderProperties);
        }
        });

        this.bucketFluid = ITEMS.register(fluid + "_bucket", () -> {
            BucketItem bucket = new BucketItem(this.sourceFluid, new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1));
            return bucket;
        });

        this.blockFluid = BLOCKS.register(fluid, () -> new LiquidBlock(
                () -> (FlowingFluid) this.sourceFluid.get(),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WATER)
                        .replaceable()
                        .noCollission()
                        .strength(100.0F)
                        .pushReaction(PushReaction.DESTROY)
                        .noLootTable()
                        .liquid()
                        .sound(SoundType.EMPTY)
        ));
    }


    public abstract AbstractAnimateFluid.Source<?> setSource(AbstractAnimateFluidInstance instance);

    public abstract AbstractAnimateFluid.Flowing<?> setFlowing(AbstractAnimateFluidInstance instance);


    public RegistryObject<FluidType> getFluidType() {
        return fluidType;
    }

    public RegistryObject<Fluid> getFlowingFluid() {
        return flowingFluid;
    }

    public RegistryObject<Fluid> getSourceFluid() {
        return sourceFluid;
    }

    public RegistryObject<Item> getBucketFluid() {
        return bucketFluid;
    }

    public RegistryObject<Block> getBlockFluid() {
        return blockFluid;
    }

    public String getFluid() {
        return fluid;
    }

    public String getModId() {
        return modId;
    }
}
