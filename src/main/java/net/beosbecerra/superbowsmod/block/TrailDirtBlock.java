package net.beosbecerra.superbowsmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TrailDirtBlock extends Block {

    private static final int LIFETIME = 300; // 10 seconds (20 ticks = 1 second)

    public TrailDirtBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {

        if (!level.isClientSide) {
            level.scheduleTick(pos, this, LIFETIME);
        }

        super.onPlace(state, level, pos, oldState, moving);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        level.removeBlock(pos, false);
    }
}