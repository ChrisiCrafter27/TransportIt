package de.chrisicrafter.transportit.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PoweredPoweredRailBlock extends PoweredRailBlock {
    public PoweredPoweredRailBlock(Properties properties) {
        super(properties, true);
    }

    @Override
    protected boolean findPoweredRailSignal(Level level, BlockPos pos, BlockState state, boolean flag, int dist) {
        return true;
    }
}
