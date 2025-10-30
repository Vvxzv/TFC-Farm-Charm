package net.vvxzv.tfc_farm_charm.common.block.decay;

import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodData;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.common.capabilities.food.TFCFoodData;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class DecayingFoodBlock extends DecayingFacingBlock{
    public static final DirectionProperty FACING;
    public static final IntegerProperty BITES;
    private int eat;
    private final int maxBites;
    private final VoxelShape SHAPE = Shapes.box((double)0.1875F, (double)0.0F, (double)0.1875F, (double)0.8125F, (double)0.875F, (double)0.8125F);
    public DecayingFoodBlock(ExtendedProperties properties, int eat, int maxBites, Supplier<? extends Block> rotted) {
        super(properties, rotted);
        this.eat = eat;
        this.maxBites = maxBites;
        this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(BITES, 0)).setValue(FACING, Direction.NORTH));
    }
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return !((Player) Objects.requireNonNull(ctx.getPlayer())).isShiftKeyDown() ? null : (BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof DecayingBlockEntity decaying && decaying.isRotten()) return InteractionResult.SUCCESS;
        if (world.isClientSide) {
            return this.tryEat(world, pos, state, player) == InteractionResult.SUCCESS ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        } else {
            return this.tryEat(world, pos, state, player);
        }
    }

    private InteractionResult tryEat(LevelAccessor world, BlockPos pos, BlockState state, Player player) {
        BlockEntity entity = world.getBlockEntity(pos);
        int bites = (Integer)state.getValue(BITES);
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            //player.getFoodData().eat(this.foodComponent.getNutrition(), this.foodComponent.getSaturationModifier());
            if(entity instanceof DecayingBlockEntity decaying) {
                if (player.getFoodData() instanceof TFCFoodData foodData && eat != bites) {
                    IFood blockFood = (IFood) Helpers.getCapability(decaying.getStack(), FoodCapability.CAPABILITY);
                    float[] nutrients = blockFood.getData().nutrients();
                    float modifier = 6F / (eat * 5F);
                    int hunger = Math.max((int) ((float) blockFood.getData().hunger() * modifier), 1);
                    float saturation = blockFood.getData().saturation() * modifier;
                    FoodData data = new FoodData(hunger, 0, saturation, nutrients[0] * modifier, nutrients[1] * modifier, nutrients[2] * modifier, nutrients[3] * modifier, nutrients[4] * modifier, 0);
                    foodData.eat(data);
                }
            }
            if (world instanceof Level) {
                Level level = (Level)world;
                if(eat != bites){
                    level.playSound((Player)null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                }
                level.gameEvent(player, GameEvent.EAT, pos);
                if (bites < this.maxBites - 1) {
                    world.setBlock(pos, (BlockState)state.setValue(BITES, bites + 1), 3);
                } else {
                    world.destroyBlock(pos, false);
                    world.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                }

                for(int count = 0; count < 10; ++count) {
                    double d0 = level.random.nextGaussian() * 0.02;
                    double d1 = level.random.nextGaussian() * 0.02;
                    double d2 = level.random.nextGaussian() * 0.02;
                    level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, d0, d1, d2);
                }
            }

            return InteractionResult.SUCCESS;
        }
    }

    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, BITES});
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.SHAPE;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingBlockEntity decaying) {
            if(state.getValue(BITES) == 0){
                if (!Helpers.isBlock(state, newState.getBlock())) {
                    Helpers.spawnItem(level, pos, decaying.getStack());
                }
            }
        }
    }

    public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
        tooltip.add(Component.translatable("tooltip.farm_and_charm.canbeplaced").withStyle(new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY}));
    }

    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        BITES = IntegerProperty.create("bites", 0, 9);
    }

}
