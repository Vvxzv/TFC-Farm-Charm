package net.vvxzv.tfc_farm_charm.common.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.fluid.BeerInstance;
import net.vvxzv.tfc_farm_charm.common.fluid.ClientFluidTypeExtensions;

import java.util.ArrayList;

public class TFCFCFluid {
    public static BeerInstance BEER_BARLEY;
    public static BeerInstance BEER_HALEY;

    @SuppressWarnings("removal")
    public static void generateFeatures(){
        BEER_BARLEY = new BeerInstance(TFCFarmCharm.MODID, "beer_barley", FluidType.Properties.create().density(1000), new ClientFluidTypeExtensions(new ResourceLocation(TFCFarmCharm.MODID, "block/fluid/beer_barley"), new ResourceLocation(TFCFarmCharm.MODID, "block/fluid/beer_barley")));
        BEER_HALEY = new BeerInstance(TFCFarmCharm.MODID, "beer_haley", FluidType.Properties.create().density(1000), new ClientFluidTypeExtensions(new ResourceLocation(TFCFarmCharm.MODID, "block/fluid/beer_haley"), new ResourceLocation(TFCFarmCharm.MODID, "block/fluid/beer_haley")));

    }

    public static ArrayList<RegistryObject<Item>> addBucketItemsToCreativeModeTab(){
        ArrayList<RegistryObject<Item>> items = new ArrayList<>();
        items.add(BEER_BARLEY.getBucketFluid());
        items.add(BEER_HALEY.getBucketFluid());
        return items;
    }
}
