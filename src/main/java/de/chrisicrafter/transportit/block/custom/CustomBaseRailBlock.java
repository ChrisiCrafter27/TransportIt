package de.chrisicrafter.transportit.block.custom;

import de.chrisicrafter.transportit.entity.custom.CustomAbstractMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class CustomBaseRailBlock extends Block implements SimpleWaterloggedBlock, CustomIForgeBaseRailBlock {
    protected static final VoxelShape FLAT_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    protected static final VoxelShape HALF_BLOCK_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final boolean isStraight;

    public static boolean isRail(Level level, BlockPos pos) {
        return isRail(level.getBlockState(pos));
    }

    public static boolean isRail(BlockState state) {
        return state.is(BlockTags.RAILS) && state.getBlock() instanceof CustomBaseRailBlock;
    }

    protected CustomBaseRailBlock(boolean straight, BlockBehaviour.Properties properties) {
        super(properties);
        this.isStraight = straight;
    }

    public boolean isStraight() {
        return this.isStraight;
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        RailShape railshape = state.is(this) ? state.getValue(this.getShapeProperty()) : null;
        RailShape railShape2 = state.is(this) ? getRailDirection(state, blockGetter, pos, null) : null;
        return railshape != null && railshape.isAscending() ? HALF_BLOCK_AABB : FLAT_AABB;
    }

    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        return canSupportRigidBlock(levelReader, pos.below());
    }

    public void onPlace(BlockState state1, Level level, BlockPos pos, BlockState state2, boolean flag) {
        if (!state2.is(state1.getBlock())) {
            this.updateState(state1, level, pos, flag);
        }
    }

    protected BlockState updateState(BlockState state, Level level, BlockPos pos, boolean flag) {
        state = this.updateDir(level, pos, state, true);
        if (this.isStraight) {
            level.neighborChanged(state, pos, this, pos, flag);
        }

        return state;
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos1, Block block, BlockPos pos2, boolean flag) {
        if (!level.isClientSide && level.getBlockState(pos1).is(this)) {
            RailShape railshape = getRailDirection(state, level, pos1, null);
            if (shouldBeRemoved(pos1, level, railshape)) {
                dropResources(state, level, pos1);
                level.removeBlock(pos1, flag);
            } else {
                this.updateState(state, level, pos1, block);
            }

        }
    }

    private static boolean shouldBeRemoved(BlockPos pos, Level level, RailShape shape) {
        if (!canSupportRigidBlock(level, pos.below())) {
            return true;
        } else {
            switch (shape) {
                case ASCENDING_EAST:
                    return !canSupportRigidBlock(level, pos.east());
                case ASCENDING_WEST:
                    return !canSupportRigidBlock(level, pos.west());
                case ASCENDING_NORTH:
                    return !canSupportRigidBlock(level, pos.north());
                case ASCENDING_SOUTH:
                    return !canSupportRigidBlock(level, pos.south());
                default:
                    return false;
            }
        }
    }

    protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {
    }

    protected BlockState updateDir(Level level, BlockPos pos, BlockState state, boolean flag) {
        if (level.isClientSide) {
            return state;
        } else {
            RailShape railshape = state.getValue(this.getShapeProperty());
            return (new CustomRailState(level, pos, state)).place(level.hasNeighborSignal(pos), flag, railshape).getState();
        }
    }

    public void onRemove(BlockState state1, Level level, BlockPos pos, BlockState state2, boolean flag) {
        if (!flag) {
            super.onRemove(state1, level, pos, state2, flag);
            if (getRailDirection(state1, level, pos, null).isAscending()) {
                level.updateNeighborsAt(pos.above(), this);
            }

            if (this.isStraight) {
                level.updateNeighborsAt(pos, this);
                level.updateNeighborsAt(pos.below(), this);
            }

        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        BlockState blockstate = super.defaultBlockState();
        Direction direction = context.getHorizontalDirection();
        boolean flag1 = direction == Direction.EAST || direction == Direction.WEST;
        return blockstate.setValue(this.getShapeProperty(), flag1 ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH).setValue(WATERLOGGED, Boolean.valueOf(flag));
    }

    /**
     * @deprecated Forge: Use {@link CustomBaseRailBlock#getRailDirection(BlockState, BlockGetter, BlockPos, CustomAbstractMinecart)} for enhanced ability
     * If you do change this property be aware that other functions in this/subclasses may break as they can make assumptions about this property
     */
    @Deprecated
    public abstract Property<RailShape> getShapeProperty();

    public BlockState updateShape(BlockState state1, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos1, BlockPos pos2) {
        if (state1.getValue(WATERLOGGED)) {
            level.scheduleTick(pos1, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state1, direction, state2, level, pos1, pos2);
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean isFlexibleRail(BlockState state, BlockGetter world, BlockPos pos) {
        return  !this.isStraight;
    }

    @Override
    public RailShape getRailDirection(BlockState state, BlockGetter world, BlockPos pos, @org.jetbrains.annotations.Nullable CustomAbstractMinecart cart) {
        return state.getValue(getShapeProperty());
    }
}
