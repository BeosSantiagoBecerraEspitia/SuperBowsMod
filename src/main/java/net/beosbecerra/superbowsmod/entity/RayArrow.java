package net.beosbecerra.superbowsmod.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;

import java.util.List;

public class RayArrow extends ModArrow {

    public static final int    RAY_DURATION    = 60;
    public static final float  DAMAGE_PER_TICK = 10.0f;
    public static final double RAY_LENGTH      = 20.0;
    public static final int    DAMAGE_INTERVAL = 3;

    // Synced to client so the renderer knows the real direction
    private static final EntityDataAccessor<Float> DIR_X =
            SynchedEntityData.defineId(RayArrow.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DIR_Y =
            SynchedEntityData.defineId(RayArrow.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DIR_Z =
            SynchedEntityData.defineId(RayArrow.class, EntityDataSerializers.FLOAT);

    public RayArrow(EntityType<? extends ModArrow> type, Level level) {
        super(type, level);
        this.maxAge       = RAY_DURATION;
        this.maxDistance  = 9999;
        this.acceleration = Vec3.ZERO;
    }

    public RayArrow(EntityType<? extends ModArrow> type, Level level, LivingEntity shooter) {
        super(type, level , shooter);
        this.maxAge       = RAY_DURATION;
        this.maxDistance  = 9999;
        this.acceleration = Vec3.ZERO;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DIR_X, 0.0f);
        this.entityData.define(DIR_Y, 0.0f);
        this.entityData.define(DIR_Z, 1.0f);
    }

    /** Called by renderer — always correct on both sides. */
    public Vec3 getSyncedDirection() {
        return new Vec3(
                this.entityData.get(DIR_X),
                this.entityData.get(DIR_Y),
                this.entityData.get(DIR_Z)
        );
    }

    @Override
    public void tick() {
        // On tick 1, capture real velocity and sync to client
        if (this.tickCount == 1) {
            Vec3 vel = this.getDeltaMovement();
            if (vel.lengthSqr() > 0.0001) {
                Vec3 dir = vel.normalize();
                this.entityData.set(DIR_X, (float) dir.x);
                this.entityData.set(DIR_Y, (float) dir.y);
                this.entityData.set(DIR_Z, (float) dir.z);
            }
        }

        super.tick();

        // Freeze after first tick
        if (this.tickCount > 1) {
            this.setDeltaMovement(Vec3.ZERO);
        }

        // Damage pulse — no particles
        if (this.tickCount % DAMAGE_INTERVAL == 0) {
            damageEntitiesAlongRay();
        }
    }

    private void damageEntitiesAlongRay() {
        Vec3 direction = getSyncedDirection();
        Vec3 origin    = this.position();
        Vec3 end       = origin.add(direction.scale(RAY_LENGTH));

        AABB rayBox = new AABB(
                Math.min(origin.x, end.x) - 1, Math.min(origin.y, end.y) - 1, Math.min(origin.z, end.z) - 1,
                Math.max(origin.x, end.x) + 1, Math.max(origin.y, end.y) + 1, Math.max(origin.z, end.z) + 1
        );

        List<LivingEntity> candidates = this.level().getEntitiesOfClass(LivingEntity.class, rayBox);
        Entity shooter = this.getOwner();

        for (LivingEntity entity : candidates) {
            if (entity == shooter) continue;

            if (entity.getBoundingBox().inflate(0.3).clip(origin, end).isPresent()) {
                DamageSource source = (shooter instanceof Player player)
                        ? this.level().damageSources().arrow(this, player)
                        : this.level().damageSources().magic();

                entity.hurt(source, DAMAGE_PER_TICK);
                applyEnchantmentEffects(entity);

                if (this.level() instanceof ServerLevel sl) {
                    Vec3 center = entity.position().add(0, entity.getBbHeight() / 2.0, 0);
                    sl.sendParticles(ParticleTypes.CRIT,
                            center.x, center.y, center.z,
                            5, 0.2, 0.2, 0.2, 0.1);
                }
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {}

    @Override
    protected void onHitBlock(BlockHitResult result) {}

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}