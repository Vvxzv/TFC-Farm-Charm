package net.vvxzv.tfc_farm_charm.common.block.decay;

import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.DecayingBlock;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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

import java.util.function.Supplier;

public class DecayingStackableBlock extends DecayingBlock {
    public static final IntegerProperty STACK_PROPERTY = IntegerProperty.create("stack", 1, 8);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(2.0F, 0.0F, 2.0F, 14.0F, 10.0F, 14.0F);
    private final int maxStack;

    public DecayingStackableBlock(ExtendedProperties properties, int maxStack, Supplier<? extends Block> rotted) {
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
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        ItemStack stack = player.getItemInHand(hand);
        if(entity instanceof DecayingBlockEntity decaying) {
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
                    return InteractionResult.SUCCESS;
                }
            } else if (stack.isEmpty()) {
                if (state.getValue(STACK_PROPERTY) > 1) {
                    world.setBlock(pos, state.setValue(STACK_PROPERTY, state.getValue(STACK_PROPERTY) - 1), 3);
                    ItemStack dropItem = decaying.getStack().copy();
                    Helpers.spawnItem(world, pos, dropItem);
                } 
                else if (state.getValue(STACK_PROPERTY) == 1) {
                    world.destroyBlock(pos, false);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        VoxelShape shape = world.getBlockState(pos.below()).getShape(world, pos.below());
        Direction direction = Direction.UP;
        return Block.isFaceFull(shape, direction);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true);
        }

    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(world, pos)) {
            world.scheduleTick(pos, this, 1);
        }

        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
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
    }
}
