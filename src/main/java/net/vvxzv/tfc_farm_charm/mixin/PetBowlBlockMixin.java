package net.vvxzv.tfc_farm_charm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.farm_and_charm.client.FarmAndCharmClient;
import net.satisfy.farm_and_charm.core.block.PetBowlBlock;
import net.satisfy.farm_and_charm.core.block.entity.PetBowlBlockEntity;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PetBowlBlock.class)
public class PetBowlBlockMixin {
    /**
     * @author Vvxzv
     * @reason fix bug: one item use on bowl will disappear
     */
    @Overwrite
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof PetBowlBlockEntity entity) {
            if (heldItem.is(Items.SHEARS) && state.getValue(PetBowlBlock.HAS_NAME_TAG)) {
                if (!level.isClientSide) {
                    level.setBlock(pos, state.setValue(PetBowlBlock.HAS_NAME_TAG, false), 3);
                    Block.popResource(level, pos, new ItemStack(Items.NAME_TAG));
                    if (!player.getAbilities().instabuild) {
                        heldItem.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
                return InteractionResult.SUCCESS;
            } else if (heldItem.is(Items.NAME_TAG) && !state.getValue(PetBowlBlock.HAS_NAME_TAG)) {
                if (!level.isClientSide) {
                    level.setBlock(pos, state.setValue(PetBowlBlock.HAS_NAME_TAG, true), 3);
                    if (!player.getAbilities().instabuild) {
                        heldItem.shrink(1);
                    }
                }
                return InteractionResult.SUCCESS;
            } else if (state.getValue(PetBowlBlock.HAS_NAME_TAG) && player.isShiftKeyDown()) {
                if (level.isClientSide) {
                    FarmAndCharmClient.openPetBowlScreen(entity);
                }
                return InteractionResult.SUCCESS;
            } else {
                if (!level.isClientSide && hand == InteractionHand.MAIN_HAND && state.getValue(PetBowlBlock.FOOD_TYPE) == GeneralUtil.FoodType.NONE) {
                    GeneralUtil.FoodType type;
                    if (heldItem.is(ObjectRegistry.CAT_FOOD.get())) {
                        type = GeneralUtil.FoodType.CAT;
                    } else if (heldItem.is(ObjectRegistry.DOG_FOOD.get())) {
                        type = GeneralUtil.FoodType.DOG;
                    } else {
                        type = GeneralUtil.FoodType.NONE;
                    }

                    if (type != GeneralUtil.FoodType.NONE) {
                        level.setBlock(pos, state.setValue(PetBowlBlock.FOOD_TYPE, type), 3);
                        entity.setItem(0, new ItemStack(heldItem.getItem()));
                        entity.onFed(heldItem);
                        if (!player.getAbilities().instabuild) {
                            heldItem.shrink(1);
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
    }
}
