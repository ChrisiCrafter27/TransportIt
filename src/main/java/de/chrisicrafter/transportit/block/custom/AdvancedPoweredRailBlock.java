package de.chrisicrafter.transportit.block.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class AdvancedPoweredRailBlock extends CustomPoweredRailBlock {
    public AdvancedPoweredRailBlock(Properties properties, PoweredRailType type) {
        super(properties, type);
    }

    @Override
    protected void registerDefaultState() {
        registerDefaultState(this.stateDefinition.any()
                .setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(POWERED, Boolean.valueOf(false))
                .setValue(WATERLOGGED, Boolean.valueOf(false))
                .setValue(CONNECTION, Connection.NONE)
                .setValue(DIRECTION, Direction.POSITIVE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(CONNECTION, DIRECTION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean isWater = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        BlockState blockstate = super.defaultBlockState();
        net.minecraft.core.Direction direction = context.getHorizontalDirection();
        boolean eastWest = direction == net.minecraft.core.Direction.EAST || direction == net.minecraft.core.Direction.WEST;
        boolean positive = direction == net.minecraft.core.Direction.EAST || direction == net.minecraft.core.Direction.SOUTH;
        return blockstate
                .setValue(getShapeProperty(), eastWest ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH)
                .setValue(DIRECTION, positive ? Direction.POSITIVE : Direction.NEGATIVE)
                .setValue(WATERLOGGED, Boolean.valueOf(isWater));
    }

    public boolean connectedAreReady() {
        return false;
    }

    public boolean ready() {
        return false;
    }
}
