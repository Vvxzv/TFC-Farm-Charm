//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.vvxzv.tfc_farm_charm.common.registry;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.fluid.Beers;
import net.vvxzv.tfc_farm_charm.common.item.CupItem;

@SuppressWarnings("unused")
public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFCFarmCharm.MODID);

    private static RegistryObject<Item> registerFoodItem(String name) {
        return registerItem(name, () -> new Item(setFood()));
    }

    private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    private static RegistryObject<Item> registerItem(String name) {
        return registerItem(name, () -> new Item(new Item.Properties()));
    }

    private static Item.Properties setFood() {
        return (new Item.Properties()).food((new FoodProperties.Builder()).build());
    }

    private static <T extends Item> RegistryObject<T> registerFluidBucket(String name, Supplier<T> item) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }

    public static final RegistryObject<Item> CUP = registerItem("cup", () -> new CupItem(new Item.Properties().stacksTo(1), TFCConfig.SERVER.jugCapacity, TFCTags.Fluids.USABLE_IN_JUG));

    public static final RegistryObject<Item> UNFINISHED_APPLE_PIE = registerFoodItem("unfinished_apple_pie");

    public static final RegistryObject<Item> UNFINISHED_BAGUETTE = registerFoodItem("unfinished_baguette");

    public static final RegistryObject<Item> UNFINISHED_BRAIDED_BREAD = registerFoodItem("unfinished_braided_bread");

    public static final RegistryObject<Item> UNFINISHED_BREAD = registerFoodItem("unfinished_bread");

    public static final RegistryObject<Item> UNFINISHED_BUN = registerFoodItem("unfinished_bun");

    public static final RegistryObject<Item> UNFINISHED_BUNDT_CAKE = registerFoodItem("unfinished_bundt_cake");

    public static final RegistryObject<Item> UNFINISHED_CHOCOLATE_TART = registerFoodItem("unfinished_chocolate_tart");

    public static final RegistryObject<Item> UNFINISHED_CROISSANT = registerFoodItem("unfinished_croissant");

    public static final RegistryObject<Item> UNFINISHED_CRUSTY_BREAD = registerFoodItem("unfinished_crusty_bread");

    public static final RegistryObject<Item> UNFINISHED_FARMERS_BREAD = registerFoodItem("unfinished_farmers_bread");

    public static final RegistryObject<Item> UNFINISHED_GLOWBERRY_TART = registerFoodItem("unfinished_glowberry_tart");

    public static final RegistryObject<Item> UNFINISHED_GRANDMOTHERS_STRAWBERRY_CAKE = registerFoodItem("unfinished_grandmothers_strawberry_cake");

    public static final RegistryObject<Item> UNFINISHED_LINZER_TART = registerFoodItem("unfinished_linzer_tart");

    public static final RegistryObject<Item> UNFINISHED_PRETZEL = registerFoodItem("unfinished_pretzel");

    public static final RegistryObject<Item> UNFINISHED_TOAST = registerFoodItem("unfinished_toast");

    public static final RegistryObject<Item> UNFINISHED_WAFFLE = registerFoodItem("unfinished_waffle");

    public static final RegistryObject<Item> BREAD_KNIFE_HEAD = registerItem("bread_knife_head");

    public static final RegistryObject<Item> PITCHFORK_HEAD = registerItem("pitchfork_head");

    public static final Map<Beers, RegistryObject<BucketItem>> BEER_FLUID_BUCKETS = Helpers.mapOfKeys(Beers.class, (fluid) -> registerFluidBucket("bucket/" + fluid.getSerializedName(), () -> new BucketItem((Fluids.BEERS.get(fluid)).source(), (new Item.Properties()).craftRemainder(net.minecraft.world.item.Items.BUCKET).stacksTo(1))));
}
