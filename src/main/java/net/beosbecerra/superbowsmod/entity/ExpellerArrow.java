package net.beosbecerra.superbowsmod.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ExpellerArrow extends Arrow {

    private static final double PUSH_RADIUS = 6.0;

    private double pushStrength = 8.0; // set from bow based on charge

    public ExpellerArrow(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    public ExpellerArrow(EntityType<? extends Arrow> type, Level level, LivingEntity shooter) {
        super( level, shooter);
    }

    public void setPushStrength(double strength) {
        this.pushStrength = strength;
    }

    private void push(Vec3 center) {

        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles( ParticleTypes.EXPLOSION_EMITTER,
                    center.x, center.y, center.z,
                    1, 0, 0, 0, 0);
        }

        AABB searchBox = new AABB(
                center.x - PUSH_RADIUS, center.y - PUSH_RADIUS, center.z - PUSH_RADIUS,
                center.x + PUSH_RADIUS, center.y + PUSH_RADIUS, center.z + PUSH_RADIUS
        );

        List<LivingEntity> entities = level().getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                e -> e.isAlive()
        );

        for (LivingEntity entity : entities) {
            Vec3 entityCenter = new Vec3(entity.getX(), entity.getY() + 1.0, entity.getZ());
            Vec3 direction = entityCenter.subtract(center);
            double distance = direction.length();

            if (distance == 0) continue;

            double strength = pushStrength * (1.0 - distance / PUSH_RADIUS);
            if (strength <= 0) continue;

            Vec3 push = direction.normalize().scale(strength);
            entity.setDeltaMovement(push);
            entity.hurtMarked = true;
        }

        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {

        push(this.position());
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        push(result.getLocation()); // use exact impact location
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}