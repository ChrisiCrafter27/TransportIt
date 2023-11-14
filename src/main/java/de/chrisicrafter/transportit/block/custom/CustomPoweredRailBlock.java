package de.chrisicrafter.transportit.block.custom;

import de.chrisicrafter.transportit.entity.custom.CustomAbstractMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;

import java.util.function.BiFunction;

public class CustomPoweredRailBlock extends CustomBaseRailBlock {
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<Connection> CONNECTION = EnumProperty.create("connection", Connection.class);
    public static final EnumProperty<Direction> DIRECTION = EnumProperty.create("direction", Direction.class);
    private final PoweredRailType type;

    public CustomPoweredRailBlock(BlockBehaviour.Properties properties, PoweredRailType type) {
        super(true, properties);
        this.type = type;
        registerDefaultState();
    }

    protected void registerDefaultState() {
        registerDefaultState(this.stateDefinition.any()
                .setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(POWERED, Boolean.valueOf(false))
                .setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    protected boolean findPoweredRailSignal(Level level, BlockPos pos, BlockState state, boolean direction, int distance) {
        if (distance >= 8) {
            return false;
        } else {
            if(type == PoweredRailType.POWERED_POWER) return true;
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            boolean flag = true;
            RailShape railshape = state.getValue(getShapeProperty());
            switch (railshape) {
                case NORTH_SOUTH:
                    if (direction) {
                        ++k;
                    } else {
                        --k;
                    }
                    break;
                case EAST_WEST:
                    if (direction) {
                        --i;
                    } else {
                        ++i;
                    }
                    break;
                case ASCENDING_EAST:
                    if (direction) {
                        --i;
                    } else {
                        ++i;
                        ++j;
                        flag = false;
                    }

                    railshape = RailShape.EAST_WEST;
                    break;
                case ASCENDING_WEST:
                    if (direction) {
                        --i;
                        ++j;
                        flag = false;
                    } else {
                        ++i;
                    }

                    railshape = RailShape.EAST_WEST;
                    break;
                case ASCENDING_NORTH:
                    if (direction) {
                        ++k;
                    } else {
                        --k;
                        ++j;
                        flag = false;
                    }

                    railshape = RailShape.NORTH_SOUTH;
                    break;
                case ASCENDING_SOUTH:
                    if (direction) {
                        ++k;
                        ++j;
                        flag = false;
                    } else {
                        --k;
                    }

                    railshape = RailShape.NORTH_SOUTH;
            }

            if (this.isSameRailWithPower(level, new BlockPos(i, j, k), direction, distance, railshape)) {
                return true;
            } else {
                return flag && this.isSameRailWithPower(level, new BlockPos(i, j - 1, k), direction, distance, railshape);
            }
        }
    }

    protected boolean isSameRailWithPower(Level level, BlockPos pos, boolean direction, int distance, RailShape railShape) {
        BlockState blockstate = level.getBlockState(pos);
        if (!(blockstate.getBlock() instanceof CustomPoweredRailBlock other)) {
            return false;
        } else {
            RailShape railshape = other.getRailDirection(blockstate, level, pos, null);
            if (railShape != RailShape.EAST_WEST || railshape != RailShape.NORTH_SOUTH && railshape != RailShape.ASCENDING_NORTH && railshape != RailShape.ASCENDING_SOUTH) {
                if (railShape != RailShape.NORTH_SOUTH || railshape != RailShape.EAST_WEST && railshape != RailShape.ASCENDING_EAST && railshape != RailShape.ASCENDING_WEST) {
                    if (type == other.getType()) {
                        return level.hasNeighborSignal(pos) || other.findPoweredRailSignal(level, pos, blockstate, direction, distance + 1);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {
        boolean flag = state.getValue(POWERED);
        boolean flag1 = level.hasNeighborSignal(pos) || this.findPoweredRailSignal(level, pos, state, true, 0) || this.findPoweredRailSignal(level, pos, state, false, 0);
        if (flag1 != flag) {
            level.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(flag1)), 3);
            level.updateNeighborsAt(pos.below(), this);
            if (state.getValue(getShapeProperty()).isAscending()) {
                level.updateNeighborsAt(pos.above(), this);
            }
        }
    }

    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                switch ((RailShape)state.getValue(SHAPE)) {
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
                    case NORTH_SOUTH: //Forge fix: MC-196102
                    case EAST_WEST:
                        return state;
                }
            case COUNTERCLOCKWISE_90:
                switch ((RailShape)state.getValue(SHAPE)) {
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_SOUTH);
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
                }
            case CLOCKWISE_90:
                switch ((RailShape)state.getValue(SHAPE)) {
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_SOUTH);
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

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(getShapeProperty(), POWERED, WATERLOGGED);
    }

    public PoweredRailType getType() {
        return type;
    }

    public enum PoweredRailType {
        POWER (true, 8, (rail, minecart) -> false),
        POWERED_POWER (false, -1, (rail, minecart) -> true),
        ACTIVATOR (true, 8, (rail, minecart) -> false),
        STATION (false, -1, (rail, minecart) -> rail instanceof AdvancedPoweredRailBlock advancedRail && advancedRail.connectedAreReady() && advancedRail.ready()),
        BRAKE (false, -1, (rail, minecart) -> rail instanceof AdvancedPoweredRailBlock advancedRail && advancedRail.connectedAreReady() && advancedRail.ready());

        public final boolean redstonePowered;
        public final int maxDistance;
        public final BiFunction<CustomPoweredRailBlock, CustomAbstractMinecart, Boolean> powered;

        PoweredRailType(boolean redstonePowered, int maxDistance, BiFunction<CustomPoweredRailBlock, CustomAbstractMinecart, Boolean> powered) {
            this.redstonePowered = redstonePowered;
            this.maxDistance = maxDistance;
            this.powered = powered;
        }
    }

    public enum Connection implements StringRepresentable {
        NONE ("none"),
        UP ("up"),
        DOWN ("down"),
        BOTH ("both");

        private final String name;

        Connection(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum Direction implements StringRepresentable {
        POSITIVE ("positive"),
        NEGATIVE ("negative");

        private final String name;

        Direction(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
