package net.vvxzv.tfc_farm_charm.common.block.decay;

import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.DecayingBlock;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import java.util.function.Supplier;

public class DecayingStackableEatableBlock extends DecayingBlock {
    public static final IntegerProperty STACK_PROPERTY = IntegerProperty.create("stack", 1, 8);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(2.0F, 0.0F, 2.0F, 14.0F, 10.0F, 14.0F);
    private final int maxStack;

    public DecayingStackableEatableBlock(ExtendedProperties properties, int maxStack, Supplier<? extends Block> rotted) {
        super(properties, rotted);
        this.maxStack = maxStack;
        this.registerDefaultState(this.stateDefinition.any().setValue(STACK_PROPERTY, 1).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STACK_PROPERTY, FACING);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate((Direction)state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof DecayingBlockEntity decaying){
            ItemStack foodItem = decaying.getStack();
            ItemStack eatItem = foodItem.copy();
            if (player.isShiftKeyDown() && stack.isEmpty() && !decaying.isRotten()) {
                if (!world.isClientSide) {
                    if (state.getValue(STACK_PROPERTY) > 1) {
                        world.setBlock(pos, state.setValue(STACK_PROPERTY, state.getValue(STACK_PROPERTY) - 1), 3);
                    } else {
                        decaying.setStack(ItemStack.EMPTY);
                        world.removeBlock(pos, false);
                    }
                    player.eat(world, eatItem);
                    world.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);
                }

                if (world.isClientSide) {
                    for (int i = 0; i < 10; ++i) {
                        double rx = world.random.nextDouble() - (double) 0.5F;
                        double ry = world.random.nextDouble();
                        double rz = world.random.nextDouble() - (double) 0.5F;
                        Vector3d velocity = (new Vector3d(rx, ry, rz)).mul(0.1);
                        world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(this.asItem())), (double) pos.getX() + (double) 0.5F, (double) pos.getY() + 0.7, (double) pos.getZ() + (double) 0.5F, velocity.x, velocity.y, velocity.z);
                    }
                }

                return InteractionResult.sidedSuccess(world.isClientSide);
            } else {
                if (stack.getItem() == this.asItem()) {
                    IFood handFood = Helpers.getCapability(stack, FoodCapability.CAPABILITY);
                    IFood blockFood = Helpers.getCapability(decaying.getStack(), FoodCapability.CAPABILITY);
                    if (state.getValue(STACK_PROPERTY) < this.maxStack && !handFood.isRotten() && !decaying.isRotten()) {
                        ItemStack setItem = handFood.getCreationDate() < blockFood.getCreationDate()? stack: decaying.getStack();
                        decaying.setStack(setItem);
                        world.setBlock(pos, state.setValue(STACK_PROPERTY, state.getValue(STACK_PROPERTY) + 1), 3);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }

                        for(int i = 0; i < 8; ++i) {
                            double angle = world.random.nextDouble() * Math.PI * (double)2.0F;
                            double speed = 0.1 + world.random.nextDouble() * 0.1;
                            double dx = Math.cos(angle) * speed;
                            double dy = 0.05;
                            double dz = Math.sin(angle) * speed;
                            world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, dx, dy, dz);
                        }

                        return InteractionResult.SUCCESS;
                    }
                } else if (stack.isEmpty()) {
                    if (state.getValue(STACK_PROPERTY) > 1) {
                        world.setBlock(pos, state.setValue(STACK_PROPERTY, state.getValue(STACK_PROPERTY) - 1), 3);
                        ItemStack dropItem = decaying.getStack().copy();
                        Helpers.spawnItem(world, pos, dropItem);
                    } else if (state.getValue(STACK_PROPERTY) == 1) {
                        world.destroyBlock(pos, false);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingBlockEntity decaying) {
            if (!Helpers.isBlock(state, newState.getBlock())) {
                int count = state.getValue(STACK_PROPERTY);
                ItemStack stack = decaying.getStack();
                stack.setCount(count);
                Helpers.spawnItem(level, pos, stack);
            }
        }

        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            level.removeBlockEntity(pos);
        }
    }
}
