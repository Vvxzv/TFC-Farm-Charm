package net.vvxzv.tfc_farm_charm.common.block.decay;

import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.capabilities.food.FoodData;
import net.dries007.tfc.common.capabilities.food.TFCFoodData;
import net.dries007.tfc.util.Helpers;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.bakery.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.registry.MobEffectRegistry;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DecayingBreadBasketBlock extends DecayingEatableBoxBlock{
    public static final FoodData BREAD_EAT = new FoodData(3, 0.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.1F, 0.0F);

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 4);
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.0625F, 0.8125F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.1875F, 0.0625F, 0.125F, 0.8125F, 0.5625F, 0.1875F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.1875F, 0.0625F, 0.8125F, 0.8125F, 0.5625F, 0.875F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125F, 0.0625F, 0.1875F, 0.1875F, 0.5625F, 0.8125F), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.8125F, 0.0625F, 0.1875F, 0.875F, 0.5625F, 0.8125F), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap(), (map) -> {
        for(Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    public DecayingBreadBasketBlock(ExtendedProperties properties, Supplier<? extends Block> rotted) {
        super(properties, rotted);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof DecayingBlockEntity decaying && decaying.isRotten()) {
            return InteractionResult.SUCCESS;
        }
        if (world.isClientSide) {
            if (this.tryEat(world, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (itemStack.isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return this.tryEat(world, pos, state, player);
    }

    private InteractionResult tryEat(Level world, BlockPos pos, BlockState state, Player player) {
        int bites = state.getValue(BITES);
        world.gameEvent(player, GameEvent.EAT, pos);
        if (bites < 4) {
            world.playSound(null, pos, SoundEvents.FOX_EAT, SoundSource.PLAYERS, 0.5F, world.getRandom().nextFloat() * 0.1F + 0.9F);
            world.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            if(player.getFoodData() instanceof TFCFoodData foodData){
                foodData.eat(BREAD_EAT);
                player.addEffect(new MobEffectInstance(MobEffectRegistry.SUSTENANCE.get(), 6000, 0));
            }
        } else {
            world.destroyBlock(pos, false);
            ItemStack bowlStack = new ItemStack(ObjectRegistry.TRAY.get());
            ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, bowlStack);
            world.addFreshEntity(itemEntity);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BITES);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingBlockEntity decaying) {
            if(state.getValue(BITES) == 0){
                if (!Helpers.isBlock(state, newState.getBlock())) {
                    if(decaying.isRotten()){
                        Helpers.spawnItem(level, pos, new ItemStack(ObjectRegistry.TRAY.get()));
                    }
                    else Helpers.spawnItem(level, pos, decaying.getStack());
                }
            }
        }

        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            level.removeBlockEntity(pos);
        }
    }
}
