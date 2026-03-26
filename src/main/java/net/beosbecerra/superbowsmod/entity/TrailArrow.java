package net.beosbecerra.superbowsmod.entity;


import net.beosbecerra.superbowsmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TrailArrow extends Arrow {


    private final List<Vec3> positionHistory = new ArrayList<>();

    private static final int DELAY = 4;   // distance behind arrow
    private static final int STEPS = 5;   // interpolation smoothness

    public TrailArrow(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    public TrailArrow(Level level, LivingEntity shooter) {
        super(level, shooter);


    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {

            // store current position
            positionHistory.add(new Vec3(this.getX(), this.getY(), this.getZ()));

            // wait until we have enough history
            if (positionHistory.size() <= DELAY) {
                return;
            }

            // take two delayed positions
            Vec3 start = positionHistory.remove(0);
            Vec3 end = positionHistory.get(0);

            double dx = end.x - start.x;
            double dy = end.y - start.y;
            double dz = end.z - start.z;

            for (int i = 0; i <= STEPS; i++) {

                double x = start.x + dx * (i / (double) STEPS);
                double y = start.y + dy * (i / (double) STEPS);
                double z = start.z + dz * (i / (double) STEPS);

                BlockPos pos = new BlockPos(
                        Mth.floor(x),
                        (Mth.floor(y)-2),
                        Mth.floor(z)
                );


                    level().setBlock(pos, ModBlocks.TRAIL_DIRT.get().defaultBlockState(), 3);

            }
        }
    }
}
