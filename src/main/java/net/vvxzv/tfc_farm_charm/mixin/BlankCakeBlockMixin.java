package net.vvxzv.tfc_farm_charm.mixin;

import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.bakery.core.block.cake.BlankCakeBlock;
import net.satisfy.bakery.core.registry.ObjectRegistry;
import net.satisfy.bakery.core.registry.SoundEventRegistry;
import net.satisfy.bakery.core.registry.TagsRegistry;
import net.vvxzv.tfc_farm_charm.common.registry.TFCFCBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlankCakeBlock.class)
public class BlankCakeBlockMixin {
    /**
     * @author Vvxzv
     * @reason compat
     */
    @Overwrite
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            ItemStack itemStack = player.getItemInHand(hand);
            Item item = itemStack.getItem();
            boolean isCake = state.getValue(BlankCakeBlock.CAKE);
            boolean isCupcake = state.getValue(BlankCakeBlock.CUPCAKE);
            boolean isCookie = state.getValue(BlankCakeBlock.COOKIE);
            if (item instanceof BlockItem) {
                Block block = ((BlockItem)item).getBlock();
                boolean matched = false;
                if (isCake) {
                    if (block == TFCFCBlock.STRAWBERRY_JAM.get()) {
                        world.setBlock(pos, TFCFCBlock.STRAWBERRY_CAKE.get().defaultBlockState(), 3);
                        world.levelEvent(2001, pos, Block.getId(TFCFCBlock.STRAWBERRY_CAKE.get().defaultBlockState()));
                        ItemStack stack = new ItemStack(TFCFCBlock.STRAWBERRY_CAKE.get());
                        BlockEntity be = world.getBlockEntity(pos);
                        if (be instanceof DecayingBlockEntity decaying) {
                            decaying.setStack(stack);
                        }
                        matched = true;
                        if (!player.getInventory().add(ObjectRegistry.JAR.get().asItem().getDefaultInstance())) {
                            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ObjectRegistry.JAR.get().asItem().getDefaultInstance()));
                        }
                    }
                    else if (block == TFCFCBlock.CHOCOLATE_JAM.get()) {
                        world.setBlock(pos, TFCFCBlock.CHOCOLATE_CAKE.get().defaultBlockState(), 3);
                        world.levelEvent(2001, pos, Block.getId(TFCFCBlock.CHOCOLATE_CAKE.get().defaultBlockState()));
                        ItemStack stack = new ItemStack(TFCFCBlock.CHOCOLATE_CAKE.get());
                        BlockEntity be = world.getBlockEntity(pos);
                        if (be instanceof DecayingBlockEntity decaying) {
                            decaying.setStack(stack);
                        }
                        matched = true;
                        if (!player.getInventory().add(ObjectRegistry.JAR.get().asItem().getDefaultInstance())) {
                            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ObjectRegistry.JAR.get().asItem().getDefaultInstance()));
                        }
                    }
                    else if (block == TFCFCBlock.SWEETBERRY_JAM.get()) {
                        world.setBlock(pos, TFCFCBlock.SWEETBERRY_CAKE.get().defaultBlockState(), 3);
                        world.levelEvent(2001, pos, Block.getId(TFCFCBlock.SWEETBERRY_CAKE.get().defaultBlockState()));
                        ItemStack stack = new ItemStack(TFCFCBlock.SWEETBERRY_CAKE.get());
                        BlockEntity be = world.getBlockEntity(pos);
                        if (be instanceof DecayingBlockEntity decaying) {
                            decaying.setStack(stack);
                        }
                        matched = true;
                        if (!player.getInventory().add(ObjectRegistry.JAR.get().asItem().getDefaultInstance())) {
                            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ObjectRegistry.JAR.get().asItem().getDefaultInstance()));
                        }
                    }
                }
                else if (isCupcake) {
                    if (block == TFCFCBlock.STRAWBERRY_JAM.get()) {
                        world.setBlock(pos, ObjectRegistry.STRAWBERRY_CUPCAKE_BLOCK.get().defaultBlockState(), 3);
                        world.levelEvent(2001, pos, Block.getId(ObjectRegistry.STRAWBERRY_CUPCAKE_BLOCK.get().defaultBlockState()));
                        matched = true;
                        if (!player.getInventory().add(ObjectRegistry.JAR.get().asItem().getDefaultInstance())) {
                            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ObjectRegistry.JAR.get().asItem().getDefaultInstance()));
                        }
                    }
                    else if (block == TFCFCBlock.APPLE_JAM.get()) {
                        world.setBlock(pos, ObjectRegistry.APPLE_CUPCAKE_BLOCK.get().defaultBlockState(), 3);
                        world.levelEvent(2001, pos, Block.getId(ObjectRegistry.APPLE_CUPCAKE_BLOCK.get().defaultBlockState()));
                        matched = true;
                        if (!player.getInventory().add(ObjectRegistry.JAR.get().asItem().getDefaultInstance())) {
                            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ObjectRegistry.JAR.get().asItem().getDefaultInstance()));
                        }
                    }
                    else if (block == TFCFCBlock.SWEETBERRY_JAM.get()) {
                        world.setBlock(pos, ObjectRegistry.SWEETBERRY_CUPCAKE_BLOCK.get().defaultBlockState(), 3);
                        world.levelEvent(2001, pos, Block.getId(ObjectRegistry.SWEETBERRY_CUPCAKE_BLOCK.get().defaultBlockState()));
                        matched = true;
                        if (!player.getInventory().add(ObjectRegistry.JAR.get().asItem().getDefaultInstance())) {
                            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ObjectRegistry.JAR.get().asItem().getDefaultInstance()));
                        }
                    }
                }
                else if (isCookie) {
                    if (block == TFCFCBlock.STRAWBERRY_JAM.get()) {
                        world.setBlock(pos, ObjectRegistry.STRAWBERRY_COOKIE_BLOCK.get().defaultBlockState(), 3);
                        world.levelEvent(2001, pos, Block.getId(ObjectRegistry.STRAWBERRY_COOKIE_BLOCK.get().defaultBlockState()));
                        matched = true;
                        if (!player.getInventory().add(ObjectRegistry.JAR.get().asItem().getDefaultInstance())) {
                            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ObjectRegistry.JAR.get().asItem().getDefaultInstance()));
                        }
                    }
                    else if (block == TFCFCBlock.CHOCOLATE_JAM.get()) {
                        world.setBlock(pos, (ObjectRegistry.CHOCOLATE_COOKIE_BLOCK.get()).defaultBlockState(), 3);
                        world.levelEvent(2001, pos, Block.getId((ObjectRegistry.CHOCOLATE_COOKIE_BLOCK.get()).defaultBlockState()));
                        matched = true;
                        if (!player.getInventory().add((ObjectRegistry.JAR.get()).asItem().getDefaultInstance())) {
                            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), (ObjectRegistry.JAR.get()).asItem().getDefaultInstance()));
                        }
                    }
                    else if (block == TFCFCBlock.SWEETBERRY_JAM.get()) {
                        world.setBlock(pos, (ObjectRegistry.SWEETBERRY_COOKIE_BLOCK.get()).defaultBlockState(), 3);
                        world.levelEvent(2001, pos, Block.getId((ObjectRegistry.SWEETBERRY_COOKIE_BLOCK.get()).defaultBlockState()));
                        matched = true;
                        if (!player.getInventory().add((ObjectRegistry.JAR.get()).asItem().getDefaultInstance())) {
                            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), (ObjectRegistry.JAR.get()).asItem().getDefaultInstance()));
                        }
                    }
                }

                if (matched) {
                    world.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(false);
                }
            }
            else {
                if (isCake && item == ObjectRegistry.CHOCOLATE_TRUFFLE.get()) {
                    player.getCooldowns().addCooldown(item, 20);
                    world.setBlock(pos, TFCFCBlock.CHOCOLATE_GATEAU.get().defaultBlockState(), 3);
                    world.levelEvent(2001, pos, Block.getId(TFCFCBlock.CHOCOLATE_GATEAU.get().defaultBlockState()));
                    ItemStack stack = new ItemStack(TFCFCBlock.CHOCOLATE_GATEAU.get());
                    BlockEntity be = world.getBlockEntity(pos);
                    if (be instanceof DecayingBlockEntity decaying) {
                        decaying.setStack(stack);
                    }
                    world.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(false);
                }

                if (isCake && itemStack.is(TagsRegistry.KNIVES)) {
                    world.setBlock(pos, state.setValue(BlankCakeBlock.CAKE, false).setValue(BlankCakeBlock.CUPCAKE, true), 3);
                    world.levelEvent(2001, pos, Block.getId(state));
                    world.playSound(null, pos, SoundEventRegistry.CAKE_CUT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.sidedSuccess(false);
                }

                if (isCupcake && item == ObjectRegistry.ROLLING_PIN.get()) {
                    world.setBlock(pos, state.setValue(BlankCakeBlock.CUPCAKE, false).setValue(BlankCakeBlock.COOKIE, true), 3);
                    world.levelEvent(2001, pos, Block.getId(state));
                    world.playSound(null, pos, SoundEvents.GENERIC_BIG_FALL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.sidedSuccess(false);
                }
            }
        }

        return InteractionResult.PASS;
    }
}
