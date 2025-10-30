//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.vvxzv.tfc_farm_charm.common.registry;

import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.satisfy.farm_and_charm.core.item.food.EffectBlockItem;
import net.satisfy.farm_and_charm.core.registry.MobEffectRegistry;

public class TFCFCItem {
    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> UNFINISHED_APPLE_PIE;
    public static final RegistryObject<Item> UNFINISHED_BAGUETTE;
    public static final RegistryObject<Item> UNFINISHED_BRAIDED_BREAD;
    public static final RegistryObject<Item> UNFINISHED_BREAD;
    public static final RegistryObject<Item> UNFINISHED_BUN;
    public static final RegistryObject<Item> UNFINISHED_BUNDT_CAKE;
    public static final RegistryObject<Item> UNFINISHED_CHOCOLATE_TART;
    public static final RegistryObject<Item> UNFINISHED_CROISSANT;
    public static final RegistryObject<Item> UNFINISHED_CRUSTY_BREAD;
    public static final RegistryObject<Item> UNFINISHED_FARMERS_BREAD;
    public static final RegistryObject<Item> UNFINISHED_GINGERBREAD;
    public static final RegistryObject<Item> UNFINISHED_GLOWBERRY_TART;
    public static final RegistryObject<Item> UNFINISHED_GRANDMOTHERS_STRAWBERRY_CAKE;
    public static final RegistryObject<Item> UNFINISHED_LINZER_TART;
    public static final RegistryObject<Item> UNFINISHED_PRETZEL;
    public static final RegistryObject<Item> UNFINISHED_TOAST;
    public static final RegistryObject<Item> UNFINISHED_WAFFLE;
    public static final RegistryObject<Item> CRUSTY_BREAD;
    public static final RegistryObject<Item> BREAD;
    public static final RegistryObject<Item> BAGUETTE;
    public static final RegistryObject<Item> TOAST;
    public static final RegistryObject<Item> BRAIDED_BREAD;
    public static final RegistryObject<Item> BUN;
    public static final RegistryObject<Item> WAFFLE;
    public static final RegistryObject<Item> PORK_KNUCKLE;
    public static final RegistryObject<Item> FRIED_CHICKEN;
    public static final RegistryObject<Item> DUMPLINGS;
    public static final RegistryObject<Item> HALF_CHICKEN;
    public static final RegistryObject<Item> MASHED_POTATOES;
    public static final RegistryObject<Item> POTATO_SALAD;
    public static final RegistryObject<Item> OAT_PANCAKE;
    public static final RegistryObject<Item> FARMERS_BREAKFAST;
    public static final RegistryObject<Item> BAKED_LAMB_HAM;
    public static final RegistryObject<Item> POTATO_WITH_ROAST_MEAT;
    public static final RegistryObject<Item> STUFFED_CHICKEN;
    public static final RegistryObject<Item> STUFFED_RABBIT;
    public static final RegistryObject<Item> FARMERS_BREAD;
    public static final RegistryObject<Item> ROASTED_CORN;
    public static final RegistryObject<Item> GRANDMOTHERS_STRAWBERRY_CAKE;

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

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "tfc_farm_charm");
        UNFINISHED_APPLE_PIE = registerFoodItem("unfinished_apple_pie");
        UNFINISHED_BAGUETTE = registerFoodItem("unfinished_baguette");
        UNFINISHED_BRAIDED_BREAD = registerFoodItem("unfinished_braided_bread");
        UNFINISHED_BREAD = registerFoodItem("unfinished_bread");
        UNFINISHED_BUN = registerFoodItem("unfinished_bun");
        UNFINISHED_BUNDT_CAKE = registerFoodItem("unfinished_bundt_cake");
        UNFINISHED_CHOCOLATE_TART = registerFoodItem("unfinished_chocolate_tart");
        UNFINISHED_CROISSANT = registerFoodItem("unfinished_croissant");
        UNFINISHED_CRUSTY_BREAD = registerFoodItem("unfinished_crusty_bread");
        UNFINISHED_FARMERS_BREAD = registerFoodItem("unfinished_farmers_bread");
        UNFINISHED_GINGERBREAD = registerFoodItem("unfinished_gingerbread");
        UNFINISHED_GLOWBERRY_TART = registerFoodItem("unfinished_glowberry_tart");
        UNFINISHED_GRANDMOTHERS_STRAWBERRY_CAKE = registerFoodItem("unfinished_grandmothers_strawberry_cake");
        UNFINISHED_LINZER_TART = registerFoodItem("unfinished_linzer_tart");
        UNFINISHED_PRETZEL = registerFoodItem("unfinished_pretzel");
        UNFINISHED_TOAST = registerFoodItem("unfinished_toast");
        UNFINISHED_WAFFLE = registerFoodItem("unfinished_waffle");
        CRUSTY_BREAD = registerItem("crusty_bread", () -> new EffectBlockItem((Block)TFCFCBlock.CRUSTY_BREAD.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 4800)));
        BREAD = registerItem("bread", () -> new EffectBlockItem((Block)TFCFCBlock.BREAD.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 4200)));
        BAGUETTE = registerItem("baguette", () -> new EffectBlockItem((Block)TFCFCBlock.BAGUETTE.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 4200)));
        TOAST = registerItem("toast", () -> new EffectBlockItem((Block)TFCFCBlock.TOAST.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 5400)));
        BRAIDED_BREAD = registerItem("braided_bread", () -> new EffectBlockItem((Block)TFCFCBlock.BRAIDED_BREAD.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 4200)));
        BUN = registerItem("bun", () -> new EffectBlockItem((Block)TFCFCBlock.BUN.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 2800)));
        WAFFLE = registerItem("waffle", () -> new EffectBlockItem((Block)TFCFCBlock.WAFFLE.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 800)));
        PORK_KNUCKLE = registerItem("pork_knuckle", () -> new EffectBlockItem((Block)TFCFCBlock.PORK_KNUCKLE.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 4000)));
        FRIED_CHICKEN = registerItem("fried_chicken", () -> new EffectBlockItem((Block)TFCFCBlock.FRIED_CHICKEN.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 4000)));
        DUMPLINGS = registerItem("dumplings", () -> new EffectBlockItem((Block)TFCFCBlock.DUMPLINGS.get(), setEffectFood((MobEffect)MobEffectRegistry.SATIATION.get(), 6000)));
        HALF_CHICKEN = registerItem("half_chicken", () -> new EffectBlockItem((Block)TFCFCBlock.HALF_CHICKEN.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 4000)));
        MASHED_POTATOES = registerItem("mashed_potatoes", () -> new EffectBlockItem((Block)TFCFCBlock.MASHED_POTATOES.get(), setEffectFood((MobEffect)MobEffectRegistry.SATIATION.get(), 4000)));
        POTATO_SALAD = registerItem("potato_salad", () -> new EffectBlockItem((Block)TFCFCBlock.POTATO_SALAD.get(), setEffectFood((MobEffect)MobEffectRegistry.SATIATION.get(), 6000)));
        OAT_PANCAKE = registerItem("oat_pancake", () -> new EffectBlockItem((Block)TFCFCBlock.OAT_PANCAKE.get(), setEffectFood((MobEffect)MobEffectRegistry.SATIATION.get(), 2400)));
        FARMERS_BREAKFAST = registerItem("farmers_breakfast", () -> new EffectBlockItem((Block)TFCFCBlock.FARMERS_BREAKFAST.get(), setEffectFood((MobEffect)MobEffectRegistry.FARMERS_BLESSING.get(), 9600)));
        BAKED_LAMB_HAM = registerItem("baked_lamb_ham", () -> new EffectBlockItem((Block)TFCFCBlock.BAKED_LAMB_HAM.get(), setEffectFood((MobEffect)MobEffectRegistry.FEAST.get(), 4800)));
        POTATO_WITH_ROAST_MEAT = registerItem("potato_with_roast_meat", () -> new EffectBlockItem((Block)TFCFCBlock.POTATO_WITH_ROAST_MEAT.get(), setEffectFood((MobEffect)MobEffectRegistry.SUSTENANCE.get(), 3600)));
        STUFFED_CHICKEN = registerItem("stuffed_chicken", () -> new EffectBlockItem((Block)TFCFCBlock.STUFFED_CHICKEN.get(), setEffectFood((MobEffect)MobEffectRegistry.FEAST.get(), 9600)));
        STUFFED_RABBIT = registerItem("stuffed_rabbit", () -> new EffectBlockItem((Block)TFCFCBlock.STUFFED_RABBIT.get(), setEffectFood((MobEffect)MobEffectRegistry.FEAST.get(), 9600)));
        FARMERS_BREAD = registerItem("farmers_bread", () -> new BlockItem((Block)TFCFCBlock.FARMERS_BREAD.get(), setFood()));
        ROASTED_CORN = registerItem("roasted_corn", () -> new EffectBlockItem((Block)TFCFCBlock.ROASTED_CORN.get(), setEffectFood((MobEffect)MobEffectRegistry.FEAST.get(), 3600)));
        GRANDMOTHERS_STRAWBERRY_CAKE = registerItem("grandmothers_strawberry_cake", () -> new EffectBlockItem((Block)TFCFCBlock.GRANDMOTHERS_STRAWBERRY_CAKE.get(), setEffectFood((MobEffect)MobEffectRegistry.GRANDMAS_BLESSING.get(), 2400)));
    }
}
