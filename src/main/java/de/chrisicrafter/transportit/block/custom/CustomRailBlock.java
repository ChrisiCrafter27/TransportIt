package de.chrisicrafter.transportit.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

public class CustomRailBlock extends CustomBaseRailBlock {
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;

    public CustomRailBlock(BlockBehaviour.Properties p_55395_) {
        super(false, p_55395_);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {
        if (block.defaultBlockState().isSignalSource() && (new CustomRailState(level, pos, state)).countPotentialConnections() == 3) {
            this.updateDir(level, pos, state, false);
        }

    }

    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    public BlockState rotate(BlockState p_55405_, Rotation p_55406_) {
        switch (p_55406_) {
            case CLOCKWISE_180:
                switch ((RailShape)p_55405_.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return p_55405_.setValue(SHAPE, RailShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return p_55405_.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_SOUTH: //Forge fix: MC-196102
                    case EAST_WEST:
                        return p_55405_;
                }
            case COUNTERCLOCKWISE_90:
                switch ((RailShape)p_55405_.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return p_55405_.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return p_55405_.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_SOUTH:
                        return p_55405_.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.NORTH_SOUTH);
                }
            case CLOCKWISE_90:
                switch ((RailShape)p_55405_.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return p_55405_.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return p_55405_.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return p_55405_.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return p_55405_.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return p_55405_.setValue(SHAPE, RailShape.NORTH_SOUTH);
                }
            default:
                return p_55405_;
        }
    }

    public BlockState mirror(BlockState p_55402_, Mirror p_55403_) {
        RailShape railshape = p_55402_.getValue(SHAPE);
        switch (p_55403_) {
            case LEFT_RIGHT:
                switch (railshape) {
                    case ASCENDING_NORTH:
                        return p_55402_.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return p_55402_.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return p_55402_.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return p_55402_.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return p_55402_.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return p_55402_.setValue(SHAPE, RailShape.SOUTH_EAST);
                    default:
                        return super.mirror(p_55402_, p_55403_);
                }
            case FRONT_BACK:
                switch (railshape) {
                    case ASCENDING_EAST:
                        return p_55402_.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return p_55402_.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return p_55402_.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return p_55402_.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return p_55402_.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return p_55402_.setValue(SHAPE, RailShape.NORTH_WEST);
                }
        }

        return super.mirror(p_55402_, p_55403_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55408_) {
        p_55408_.add(SHAPE, WATERLOGGED);
    }
}
