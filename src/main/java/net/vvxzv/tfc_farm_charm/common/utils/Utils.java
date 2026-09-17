package net.vvxzv.tfc_farm_charm.common.utils;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blockentities.IFarmland;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.vvxzv.tfc_farm_charm.common.block.entity.DecayingFoodBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Utils {
    public static void farmlandReceiveNutrients(IFarmland farmland, float nitrogen, float phosphorous, float potassium) {
        addFarmlandNutrient(farmland, FarmlandBlockEntity.NutrientType.NITROGEN, 1, nitrogen);
        addFarmlandNutrient(farmland, FarmlandBlockEntity.NutrientType.PHOSPHOROUS, 1, phosphorous);
        addFarmlandNutrient(farmland, FarmlandBlockEntity.NutrientType.POTASSIUM, 1, potassium);
    }

    public static void addFarmlandNutrient(IFarmland farmland, FarmlandBlockEntity.NutrientType type, float cap, float amount) {
        float current = farmland.getNutrient(type);
        if (current < cap) {
            if (current + amount > cap) {
                farmland.setNutrient(type, cap);
            } else {
                farmland.addNutrient(type, amount);
            }
        }
    }

    public static boolean isBeingBurned(Level level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        if (belowState.is(AllTags.Blocks.HEAT_SOURCE)) {
            return true;
        }

        return belowState.hasProperty(BlockStateProperties.LIT) ? belowState.getValue(BlockStateProperties.LIT) : false;
    }

    public static ItemStack copyFood(LevelAccessor level, BlockPos pos, ItemStack stack) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            ItemStack blockItem = decaying.copyStack();
            return copyFood(blockItem, stack);
        }

        return stack;
    }

    public static ItemStack copyFood(ItemStack input, ItemStack output) {
        IFood inputFood = FoodCapability.get(input);
        if(inputFood != null) {
            IFood outputFood = FoodCapability.get(output);
            if(outputFood != null) {
                outputFood.setCreationDate(inputFood.getCreationDate());
                List<FoodTrait> traits = inputFood.getTraits();
                for (FoodTrait trait : traits) {
                    FoodCapability.applyTrait(outputFood, trait);
                }
            }
        }

        return output;
    }

    public static @Nullable ItemStack mergeFoodDecay(ItemStack stack1, ItemStack stack2) {
        IFood food1 = FoodCapability.get(stack1);
        IFood food2 = FoodCapability.get(stack2);
        if(food1 != null && food2 != null) {
            return food1.getCreationDate() < food2.getCreationDate()? stack1: stack2;
        }

        return null;
    }

    public static void replaceTabItem(BuildCreativeModeTabContentsEvent event, ItemLike oldItem, ItemLike newItem) {
        var entries = event.getEntries();
        var iterator = entries.iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (entry.getKey().is(oldItem.asItem())) {
                iterator.remove();
                entries.putBefore(entry.getKey(), new ItemStack(newItem), entry.getValue());
                break;
            }
        }
    }

    public static ResourceLocation getResourceLocation(String string) {
        String[] s = string.split(":");
        if(s.length == 2) {
            return ResourceLocation.fromNamespaceAndPath(s[0], s[1]);
        } else if(s.length == 1) {
            return ResourceLocation.fromNamespaceAndPath("minecraft" ,string);
        }

        throw new IllegalArgumentException("Unknow ResourceLocation: " + string);
    }
}
