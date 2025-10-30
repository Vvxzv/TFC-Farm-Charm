package net.vvxzv.tfc_farm_charm.common.block.decay;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DecayingGlowberryTartBlock extends DecayingPieBlock{
    private static final Supplier<VoxelShape> fullShapeSupplier = () -> Shapes.box((double)0.0625F, (double)0.0F, (double)0.0625F, (double)0.9375F, (double)0.3125F, (double)0.9375F);
    public static final Map<Direction, VoxelShape> FULL_SHAPE = (Map) Util.make(new HashMap(), (map) -> {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, (VoxelShape)fullShapeSupplier.get()));
        }

    });
    private static final Supplier<VoxelShape> threeShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box((double)0.0625F, (double)0.0F, (double)0.5F, (double)0.9375F, (double)0.3125F, (double)0.9375F));
        shape = Shapes.or(shape, Shapes.box((double)0.0625F, (double)0.0F, (double)0.0625F, (double)0.5F, (double)0.3125F, (double)0.5F));
        return shape;
    };
    public static final Map<Direction, VoxelShape> THREE_SHAPE = (Map)Util.make(new HashMap(), (map) -> {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, (VoxelShape)threeShapeSupplier.get()));
        }

    });
    private static final Supplier<VoxelShape> halfShapeSupplier = () -> Shapes.box((double)0.0625F, (double)0.0F, (double)0.5F, (double)0.9375F, (double)0.3125F, (double)0.9375F);
    public static final Map<Direction, VoxelShape> HALF_SHAPE = (Map)Util.make(new HashMap(), (map) -> {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, (VoxelShape)halfShapeSupplier.get()));
        }

    });
    private static final Supplier<VoxelShape> quarterShapeSupplier = () -> Shapes.box((double)0.0625F, (double)0.0F, (double)0.5F, (double)0.5F, (double)0.3125F, (double)0.9375F);
    public static final Map<Direction, VoxelShape> QUARTER_SHAPE = (Map)Util.make(new HashMap(), (map) -> {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, (VoxelShape)quarterShapeSupplier.get()));
        }

    });

    public DecayingGlowberryTartBlock(ExtendedProperties properties, Supplier<Item> slice, Supplier<? extends Block> rotted) {
        super(properties, slice, rotted);
    }


    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        int cuts = (Integer)state.getValue(CUTS);
        Map var10000;
        switch (cuts) {
            case 1 -> var10000 = THREE_SHAPE;
            case 2 -> var10000 = HALF_SHAPE;
            case 3 -> var10000 = QUARTER_SHAPE;
            default -> var10000 = FULL_SHAPE;
        }

        Map<Direction, VoxelShape> shape = var10000;
        Direction direction = (Direction)state.getValue(FACING);
        return (VoxelShape)shape.get(direction);
    }
}
