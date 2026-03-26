package net.beosbecerra.superbowsmod.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class ModArrow extends AbstractArrow {

    protected float baseDamage   = 3.0f;
    protected double maxDistance = 30.0;
    protected int maxAge = 200; // 200 ticks = 10 seconds
    protected Vec3 acceleration  = Vec3.ZERO;

    private Vec3 spawnPosition = null;

    public ModArrow(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.pickup = Pickup.DISALLOWED;
    }

    public ModArrow(EntityType<? extends AbstractArrow> type, Level level, LivingEntity shooter) {
        super(type, shooter, level);
        this.spawnPosition = this.position();
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    public void tick() {



        this.baseTick();

        // manually do what we need from Entity.tick()
        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        // store spawn position on first tick as fallback
        if (spawnPosition == null) {
            spawnPosition = this.position();
        }

        // apply acceleration
        this.setDeltaMovement(this.getDeltaMovement().add(acceleration));

        // check collision
        HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

        if (hit.getType() == HitResult.Type.ENTITY) {
            onHitEntity((EntityHitResult) hit);
        } else if (hit.getType() == HitResult.Type.BLOCK) {
            onHitBlock((BlockHitResult) hit);
        }

        // move
        Vec3 velocity = this.getDeltaMovement();
        this.setPos(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);


        // face direction of travel
        this.updateRotation();

        // despawn after maxDistance
        if (this.tickCount >= maxAge || this.position().distanceTo(spawnPosition) >= maxDistance) {
            this.discard();
        }
    }


    @Override
    protected void updateRotation() {
        Vec3 velocity = this.getDeltaMovement();
        double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

        // increment yRot by the angle we actually rotated this tick — no atan2 flip
        float targetYRot = (float)(Math.toDegrees(Math.atan2(velocity.x, velocity.z)));
        float diff = targetYRot - this.getYRot();

        // normalize diff to [-180, 180] to always take the short way around
        while (diff > 180) diff -= 360;
        while (diff < -180) diff += 360;

        this.setYRot(this.getYRot() + diff * 0.5f);
        this.setXRot((float)(Math.toDegrees(Math.atan2(velocity.y, horizontalSpeed))));
    }

    protected abstract void onHitEntity(EntityHitResult result);
    protected abstract void onHitBlock(BlockHitResult result);

    protected void applyEnchantmentEffects(Entity target) {
        if (this.isOnFire()) {
            target.setSecondsOnFire(5);
        }
    }
    public void setBaseDamage(float damage) {
        this.baseDamage = damage;
    }

}