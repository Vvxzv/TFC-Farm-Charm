package net.vvxzv.tfc_farm_charm.common.block.decay;

import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.bakery.core.registry.SoundEventRegistry;
import net.satisfy.bakery.core.registry.TagsRegistry;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DecayingPieBlock extends DecayingFacingBlock{
    public static final IntegerProperty CUTS = IntegerProperty.create("cuts", 0, 3);
    public final Supplier<Item> Slice;
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap(), (map) -> {
        for(Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    public DecayingPieBlock(ExtendedProperties properties, Supplier<Item> slice, Supplier<? extends Block> rotted) {
        super(properties, rotted);
        this.Slice = slice != null ? slice : () -> Items.AIR;
        this.registerDefaultState(this.defaultBlockState().setValue(CUTS, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CUTS);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return this.getMaxCuts() - blockState.getValue(CUTS);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public ItemStack getPieSliceItem() {
        return new ItemStack(this.Slice != null ? this.Slice.get() : Items.AIR);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof DecayingBlockEntity decaying && decaying.isRotten()) return InteractionResult.SUCCESS;
        if (!level.isClientSide && !player.isShiftKeyDown() && state.getValue(CUTS) == 0 && heldStack.isEmpty()) {
            level.removeBlock(pos, false);
            return InteractionResult.SUCCESS;
        } else if (!player.isShiftKeyDown() || !heldStack.isEmpty() && !heldStack.is(TagsRegistry.KNIVES)) {
            return !player.isShiftKeyDown() && heldStack.is(TagsRegistry.KNIVES) ? this.cutSlice(level, pos, state, player) : InteractionResult.PASS;
        } else {
            return this.consumeBite(level, pos, state, player);
        }
    }

    protected InteractionResult consumeBite(Level level, BlockPos pos, BlockState state, Player playerIn) {
        if (!playerIn.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            ItemStack sliceStack = this.getPieSliceItem();
            FoodProperties sliceFood = sliceStack.getItem().getFoodProperties();
            playerIn.getFoodData().eat(sliceStack.getItem(), sliceStack);
            if (this.getPieSliceItem().getItem().isEdible() && sliceFood != null) {
                for(Pair<MobEffectInstance, Float> pair : sliceFood.getEffects()) {
                    if (!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
                        playerIn.addEffect(new MobEffectInstance(pair.getFirst()));
                    }
                }
            }

            int cuts = state.getValue(CUTS);
            if (cuts < this.getMaxCuts() - 1) {
                level.setBlock(pos, state.setValue(CUTS, cuts + 1), 3);
            } else {
                level.destroyBlock(pos, false);
            }

            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            return InteractionResult.SUCCESS;
        }
    }

    protected InteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Player player) {
        int cuts = state.getValue(CUTS);
        if (cuts < this.getMaxCuts() - 1) {
            level.setBlock(pos, state.setValue(CUTS, cuts + 1), 3);
        } else {
            level.removeBlock(pos, false);
        }

        Direction direction = player.getDirection().getOpposite();
        double xMotion = (double)direction.getStepX() * 0.13F;
        double yMotion = 0.35;
        double zMotion = (double)direction.getStepZ() * 0.13F;
        GeneralUtil.spawnSlice(level, this.getPieSliceItem(), (double)pos.getX() + (double)0.5F, (double)pos.getY() + 0.3, (double)pos.getZ() + (double)0.5F, xMotion, yMotion, zMotion);
        level.playSound(null, pos, SoundEventRegistry.CAKE_CUT.get(), SoundSource.PLAYERS, 0.75F, 0.75F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return GeneralUtil.isFullAndSolid(levelReader, blockPos);
    }

    public int getMaxCuts() {
        return 4;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
        tooltip.add(Component.translatable("tooltip.bakery.canbeplaced").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.bakery.cake_1").withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.translatable("tooltip.bakery.cake_2").withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.translatable("tooltip.bakery.cake_3").withStyle(ChatFormatting.WHITE));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingBlockEntity decaying) {
            if (!Helpers.isBlock(state, newState.getBlock()) && state.getValue(CUTS) == 0){
                Helpers.spawnItem(level, pos, decaying.getStack());
            }
        }

        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            level.removeBlockEntity(pos);
        }
    }
}
