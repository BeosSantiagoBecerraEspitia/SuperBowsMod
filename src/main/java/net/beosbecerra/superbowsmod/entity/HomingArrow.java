package net.beosbecerra.superbowsmod.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;


import java.util.Comparator;
import java.util.List;

public class HomingArrow extends ModArrow {



    public HomingArrow(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.baseDamage  = 5.0f;
        this.maxDistance = 30.0;

    }

    public HomingArrow(EntityType<? extends AbstractArrow> type, Level level, LivingEntity shooter) {
        super(type, level, shooter);
        this.baseDamage  = 5.0f;
        this.maxDistance = 30.0;
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        applyEnchantmentEffects(result.getEntity());
        result.getEntity().hurt(
                this.damageSources().thrown(this, this.getOwner()),
                baseDamage
        );
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.discard();
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }
    @Override
    public void tick() {

        Vec3 vel = this.getDeltaMovement();
        System.out.println("velocity: " + vel.x + " " + vel.y + " " + vel.z);
        System.out.println("speed: " + vel.length());

        LivingEntity target = findNearestTarget();
        if (target != null) {
            Vec3 currentVelocity = this.getDeltaMovement();
            double currentSpeed = currentVelocity.length();

            Vec3 toTarget = target.getEyePosition()
                    .subtract(this.position())
                    .normalize();

            // closer = more aggressive, farther = more gentle
            double distance = this.distanceTo(target);
            double blend = (0.8 / distance);

            Vec3 newDir = currentVelocity.normalize()
                    .add(toTarget.subtract(currentVelocity.normalize()).scale(blend))
                    .normalize();

            this.setDeltaMovement(newDir.scale(currentSpeed));
            this.acceleration = Vec3.ZERO;
        }
        super.tick();
    }

    private LivingEntity findNearestTarget() {
        AABB searchBox = this.getBoundingBox().inflate(4.0);

        List<LivingEntity> candidates = level().getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                e -> e instanceof Monster && e.isAlive() && !e.equals(this.getOwner())
        );

        return candidates.stream()
                .min(Comparator.comparingDouble(e -> e.distanceToSqr(this)))
                .orElse(null);
    }

}