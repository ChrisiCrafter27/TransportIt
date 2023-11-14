package de.chrisicrafter.transportit.block.custom;

import de.chrisicrafter.transportit.entity.custom.CustomAbstractMinecart;
import de.chrisicrafter.transportit.entity.custom.CustomMinecartCommandBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

public class CustomDetectorRailBlock extends CustomBaseRailBlock {
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final int PRESSED_CHECK_PERIOD = 20;

    public CustomDetectorRailBlock(BlockBehaviour.Properties properties) {
        super(true, properties);
        this.registerDefaultState();
    }

    protected void registerDefaultState() {
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)).setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    public boolean isSignalSource(BlockState state) {
        return true;
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide) {
            if (!state.getValue(POWERED)) {
                this.checkPressed(level, pos, state);
            }
        }
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (state.getValue(POWERED)) {
            this.checkPressed(level, pos, state);
        }
    }

    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    public int getDirectSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        if (!state.getValue(POWERED)) {
            return 0;
        } else {
            return direction == Direction.UP ? 15 : 0;
        }
    }

    private void checkPressed(Level level, BlockPos pos, BlockState state) {
        if (this.canSurvive(state, level, pos)) {
            boolean flag = state.getValue(POWERED);
            boolean flag1 = false;
            List<CustomAbstractMinecart> list = this.getInteractingMinecartOfType(level, pos, CustomAbstractMinecart.class, entity -> true);
            if (!list.isEmpty()) {
                flag1 = true;
            }

            if (flag1 && !flag) {
                BlockState blockstate = state.setValue(POWERED, Boolean.valueOf(true));
                level.setBlock(pos, blockstate, 3);
                this.updatePowerToConnected(level, pos, blockstate, true);
                level.updateNeighborsAt(pos, this);
                level.updateNeighborsAt(pos.below(), this);
                level.setBlocksDirty(pos, state, blockstate);
            }

            if (!flag1 && flag) {
                BlockState blockstate1 = state.setValue(POWERED, Boolean.valueOf(false));
                level.setBlock(pos, blockstate1, 3);
                this.updatePowerToConnected(level, pos, blockstate1, false);
                level.updateNeighborsAt(pos, this);
                level.updateNeighborsAt(pos.below(), this);
                level.setBlocksDirty(pos, state, blockstate1);
            }

            if (flag1) {
                level.scheduleTick(pos, this, 20);
            }

            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    protected void updatePowerToConnected(Level level, BlockPos pos, BlockState state, boolean flag) {
        RailState railstate = new RailState(level, pos, state);

        for(BlockPos blockpos : railstate.getConnections()) {
            BlockState blockstate = level.getBlockState(blockpos);
            level.neighborChanged(blockstate, blockpos, blockstate.getBlock(), pos, false);
        }

    }

    public void onPlace(BlockState state1, Level level, BlockPos pos, BlockState state2, boolean flag) {
        if (!state2.is(state1.getBlock())) {
            BlockState blockstate = this.updateState(state1, level, pos, flag);
            this.checkPressed(level, pos, blockstate);
        }
    }

    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (state.getValue(POWERED)) {
            List<CustomMinecartCommandBlock> list = this.getInteractingMinecartOfType(level, pos, CustomMinecartCommandBlock.class, (p_153123_) -> {
                return true;
            });
            if (!list.isEmpty()) {
                return list.get(0).getCommandBlock().getSuccessCount();
            }

            List<CustomAbstractMinecart> carts = this.getInteractingMinecartOfType(level, pos, CustomAbstractMinecart.class, e -> e.isAlive());
            if (!carts.isEmpty() && carts.get(0).getComparatorLevel() > -1) return carts.get(0).getComparatorLevel();
            List<CustomAbstractMinecart> list1 = carts.stream().filter(EntitySelector.CONTAINER_ENTITY_SELECTOR).collect(java.util.stream.Collectors.toList());
            if (!list1.isEmpty()) {
                return AbstractContainerMenu.getRedstoneSignalFromContainer((Container)list1.get(0));
            }
        }

        return 0;
    }

    private <T extends CustomAbstractMinecart> List<T> getInteractingMinecartOfType(Level level, BlockPos pos, Class<T> minecart, Predicate<Entity> entity) {
        return level.getEntitiesOfClass(minecart, this.getSearchBB(pos), entity);
    }

    private AABB getSearchBB(BlockPos pos) {
        double d0 = 0.2D;
        return new AABB((double)pos.getX() + 0.2D, (double)pos.getY(), (double)pos.getZ() + 0.2D, (double)(pos.getX() + 1) - 0.2D, (double)(pos.getY() + 1) - 0.2D, (double)(pos.getZ() + 1) - 0.2D);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                switch (state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                }
            case COUNTERCLOCKWISE_90:
                switch (state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                }
            case CLOCKWISE_90:
                switch (state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                }
            default:
                return state;
        }
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        RailShape railshape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                switch (railshape) {
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    default:
                        return super.mirror(state, mirror);
                }
            case FRONT_BACK:
                switch (railshape) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                }
        }

        return super.mirror(state, mirror);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getShapeProperty(), POWERED, WATERLOGGED);
    }
}
