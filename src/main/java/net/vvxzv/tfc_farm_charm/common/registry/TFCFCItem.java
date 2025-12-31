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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.satisfy.farm_and_charm.core.item.food.EffectBlockItem;
import net.satisfy.farm_and_charm.core.registry.MobEffectRegistry;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.fluid.Beers;
import net.vvxzv.tfc_farm_charm.common.item.CupItem;

public class TFCFCItem {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFCFarmCharm.MODID);

    private static RegistryObject<Item> registerFoodItem(String name) {
        return ITEMS.register(name, () -> new Item(setFood()));
    }

    private static RegistryObject<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }

    private static Item.Properties setFood() {
        return (new Item.Properties()).food((new FoodProperties.Builder()).build());
    }

    private static Item.Properties setEffectFood(MobEffect effect, int duration) {
        return (new Item.Properties()).food((new FoodProperties.Builder()).effect(new MobEffectInstance(effect, duration), 1.0F).build());
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

    public static final RegistryObject<Item> CRUSTY_BREAD = registerItem("crusty_bread", () -> new EffectBlockItem(TFCFCBlock.CRUSTY_BREAD.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 4800)));

    public static final RegistryObject<Item> BREAD = registerItem("bread", () -> new EffectBlockItem(TFCFCBlock.BREAD.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 4200)));

    public static final RegistryObject<Item> BAGUETTE = registerItem("baguette", () -> new EffectBlockItem(TFCFCBlock.BAGUETTE.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 4200)));

    public static final RegistryObject<Item> TOAST = registerItem("toast", () -> new EffectBlockItem(TFCFCBlock.TOAST.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 5400)));

    public static final RegistryObject<Item> BRAIDED_BREAD = registerItem("braided_bread", () -> new EffectBlockItem(TFCFCBlock.BRAIDED_BREAD.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 4200)));

    public static final RegistryObject<Item> BUN = registerItem("bun", () -> new EffectBlockItem(TFCFCBlock.BUN.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 2800)));

    public static final RegistryObject<Item> WAFFLE = registerItem("waffle", () -> new EffectBlockItem(TFCFCBlock.WAFFLE.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 800)));

    public static final RegistryObject<Item> PORK_KNUCKLE = registerItem("pork_knuckle", () -> new EffectBlockItem(TFCFCBlock.PORK_KNUCKLE.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 4000)));

    public static final RegistryObject<Item> FRIED_CHICKEN = registerItem("fried_chicken", () -> new EffectBlockItem(TFCFCBlock.FRIED_CHICKEN.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 4000)));

    public static final RegistryObject<Item> DUMPLINGS = registerItem("dumplings", () -> new EffectBlockItem(TFCFCBlock.DUMPLINGS.get(), setEffectFood(MobEffectRegistry.SATIATION.get(), 6000)));

    public static final RegistryObject<Item> HALF_CHICKEN = registerItem("half_chicken", () -> new EffectBlockItem(TFCFCBlock.HALF_CHICKEN.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 4000)));

    public static final RegistryObject<Item> MASHED_POTATOES = registerItem("mashed_potatoes", () -> new EffectBlockItem(TFCFCBlock.MASHED_POTATOES.get(), setEffectFood(MobEffectRegistry.SATIATION.get(), 4000)));

    public static final RegistryObject<Item> POTATO_SALAD = registerItem("potato_salad", () -> new EffectBlockItem(TFCFCBlock.POTATO_SALAD.get(), setEffectFood(MobEffectRegistry.SATIATION.get(), 6000)));

    public static final RegistryObject<Item> OAT_PANCAKE = registerItem("oat_pancake", () -> new EffectBlockItem(TFCFCBlock.OAT_PANCAKE.get(), setEffectFood(MobEffectRegistry.SATIATION.get(), 2400)));

    public static final RegistryObject<Item> FARMERS_BREAKFAST = registerItem("farmers_breakfast", () -> new EffectBlockItem(TFCFCBlock.FARMERS_BREAKFAST.get(), setEffectFood(MobEffectRegistry.FARMERS_BLESSING.get(), 9600)));

    public static final RegistryObject<Item> BAKED_LAMB_HAM = registerItem("baked_lamb_ham", () -> new EffectBlockItem(TFCFCBlock.BAKED_LAMB_HAM.get(), setEffectFood(MobEffectRegistry.FEAST.get(), 4800)));

    public static final RegistryObject<Item> POTATO_WITH_ROAST_MEAT = registerItem("potato_with_roast_meat", () -> new EffectBlockItem(TFCFCBlock.POTATO_WITH_ROAST_MEAT.get(), setEffectFood(MobEffectRegistry.SUSTENANCE.get(), 3600)));

    public static final RegistryObject<Item> STUFFED_CHICKEN = registerItem("stuffed_chicken", () -> new EffectBlockItem(TFCFCBlock.STUFFED_CHICKEN.get(), setEffectFood(MobEffectRegistry.FEAST.get(), 9600)));

    public static final RegistryObject<Item> STUFFED_RABBIT = registerItem("stuffed_rabbit", () -> new EffectBlockItem(TFCFCBlock.STUFFED_RABBIT.get(), setEffectFood(MobEffectRegistry.FEAST.get(), 9600)));

    public static final RegistryObject<Item> FARMERS_BREAD = registerItem("farmers_bread", () -> new BlockItem(TFCFCBlock.FARMERS_BREAD.get(), setFood()));

    public static final RegistryObject<Item> ROASTED_CORN = registerItem("roasted_corn", () -> new EffectBlockItem(TFCFCBlock.ROASTED_CORN.get(), setEffectFood(MobEffectRegistry.FEAST.get(), 3600)));

    public static final RegistryObject<Item> GRANDMOTHERS_STRAWBERRY_CAKE = registerItem("grandmothers_strawberry_cake", () -> new EffectBlockItem(TFCFCBlock.GRANDMOTHERS_STRAWBERRY_CAKE.get(), setEffectFood(MobEffectRegistry.GRANDMAS_BLESSING.get(), 2400)));

    public static final Map<Beers, RegistryObject<BucketItem>> BEER_FLUID_BUCKETS = Helpers.mapOfKeys(Beers.class, (fluid) -> registerFluidBucket("bucket/" + fluid.getSerializedName(), () -> new BucketItem((TFCFCFluid.BEERS.get(fluid)).source(), (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1))));
}
