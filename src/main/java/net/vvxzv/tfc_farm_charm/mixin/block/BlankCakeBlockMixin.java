package net.vvxzv.tfc_farm_charm.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.bakery.core.block.cake.BlankCakeBlock;
import net.satisfy.bakery.core.registry.ObjectRegistry;
import net.satisfy.bakery.core.registry.SoundEventRegistry;
import net.satisfy.bakery.core.registry.TagsRegistry;
import net.vvxzv.tfc_farm_charm.common.block.entity.DecayingFoodBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

@Mixin(BlankCakeBlock.class)
public class BlankCakeBlockMixin {

    @Final
    @Shadow(remap = false)
    public static BooleanProperty CAKE;
    @Final
    @Shadow(remap = false)
    public static BooleanProperty CUPCAKE;
    @Final
    @Shadow(remap = false)
    public static BooleanProperty COOKIE;

    /**
     * @author Vvxzv
     * @reason Ah?
     */
    @Overwrite
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;

        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        boolean isCake = state.getValue(CAKE);
        boolean isCupcake = state.getValue(CUPCAKE);
        boolean isCookie = state.getValue(COOKIE);

        if (isCake && stack.is(TagsRegistry.KNIVES)) {
            world.setBlock(pos, state.setValue(CAKE, false).setValue(CUPCAKE, true), 3);
            world.playSound(null, pos, SoundEventRegistry.CAKE_CUT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        if (isCupcake && item == ObjectRegistry.ROLLING_PIN.get()) {
            world.setBlock(pos, state.setValue(CUPCAKE, false).setValue(COOKIE, true), 3);
            world.playSound(null, pos, SoundEvents.GENERIC_BIG_FALL, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        if (isCake && item == ObjectRegistry.CHOCOLATE_TRUFFLE.get()) {
            world.setBlock(pos, ObjectRegistry.CHOCOLATE_GATEAU.get().defaultBlockState(), 3);
            world.levelEvent(2001, pos, Block.getId(ObjectRegistry.CHOCOLATE_GATEAU.get().defaultBlockState()));
            if(world.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decay) {
                decay.setStack(new ItemStack(ObjectRegistry.CHOCOLATE_GATEAU.get()));
            }
            world.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            player.getCooldowns().addCooldown(item, 20);
            shrinkItem(player, stack);
            return InteractionResult.SUCCESS;
        }

        Block resultBlock = getTransformedBlock(item, isCake, isCupcake, isCookie);
        if (resultBlock != null) {
            applyTransformation(world, pos, resultBlock, player, stack, isCake);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Unique
    private void shrinkItem(Player player, ItemStack stack) {
        if (!player.isCreative()) stack.shrink(1);
    }

    @Unique
    private void giveJar(Level world, Player player, BlockPos pos) {
        ItemStack jar = ObjectRegistry.JAR.get().asItem().getDefaultInstance();
        if (!player.getInventory().add(jar)) {
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), jar));
        }
    }

    @Unique
    private Block getTransformedBlock(Item item, boolean isCake, boolean isCupcake, boolean isCookie) {
        if (!(item instanceof BlockItem blockItem)) return null;
        Block input = blockItem.getBlock();

        if (isCake) {
            if (input == ObjectRegistry.STRAWBERRY_JAM.get()) return ObjectRegistry.STRAWBERRY_CAKE.get();
            if (input == ObjectRegistry.CHOCOLATE_JAM.get()) return ObjectRegistry.CHOCOLATE_CAKE.get();
            if (input == ObjectRegistry.SWEETBERRY_JAM.get()) return ObjectRegistry.SWEETBERRY_CAKE.get();
        } else if (isCupcake) {
            if (input == ObjectRegistry.STRAWBERRY_JAM.get()) return ObjectRegistry.STRAWBERRY_CUPCAKE_BLOCK.get();
            if (input == ObjectRegistry.APPLE_JAM.get()) return ObjectRegistry.APPLE_CUPCAKE_BLOCK.get();
            if (input == ObjectRegistry.SWEETBERRY_JAM.get()) return ObjectRegistry.SWEETBERRY_CUPCAKE_BLOCK.get();
        } else if (isCookie) {
            if (input == ObjectRegistry.STRAWBERRY_JAM.get()) return ObjectRegistry.STRAWBERRY_COOKIE_BLOCK.get();
            if (input == ObjectRegistry.CHOCOLATE_JAM.get()) return ObjectRegistry.CHOCOLATE_COOKIE_BLOCK.get();
            if (input == ObjectRegistry.SWEETBERRY_JAM.get()) return ObjectRegistry.SWEETBERRY_COOKIE_BLOCK.get();
        }
        return null;
    }

    @Unique
    private void applyTransformation(Level world, BlockPos pos, Block resultBlock, Player player, ItemStack stack, boolean isCake) {
        BlockState newState = resultBlock.defaultBlockState();
        world.setBlock(pos, newState, 3);
        world.levelEvent(2001, pos, Block.getId(newState));
        if(isCake) {
            if(world.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decay) {
                decay.setStack(new ItemStack(resultBlock.asItem()));
            }
        }
        world.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        shrinkItem(player, stack);
        giveJar(world, player, pos);
    }
}
