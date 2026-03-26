package net.beosbecerra.superbowsmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;


public class OrbitArrow extends ModArrow {

    private static final double MIN_DISTANCE = 0.0; // only steers if farther than this from player

    public OrbitArrow(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.baseDamage  = 5.0f;
        this.maxDistance = Double.MAX_VALUE;
        this.maxAge = 1200; // 1 minutes
    }

    public OrbitArrow(EntityType<? extends AbstractArrow> type, Level level, LivingEntity shooter) {
        super(type, level, shooter);
        this.baseDamage  = 5.0f;
        this.maxDistance = Double.MAX_VALUE;
        this.maxAge = 1200; // 1 minutes
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
        //this.discard();
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void tick() {


        LivingEntity owner = (LivingEntity) this.getOwner();

        if (owner != null && this.distanceTo(owner) > MIN_DISTANCE) {
            Vec3 currentVelocity = this.getDeltaMovement();
            double currentSpeed = currentVelocity.length();

            Vec3 toPlayer = owner.getEyePosition()
                    .subtract(this.position())
                    .normalize();

            double distance = this.distanceTo(owner);
            double blend = 0.01 * (distance * distance);

            Vec3 newDir = currentVelocity.normalize()
                    .add(toPlayer.subtract(currentVelocity.normalize()).scale(blend))
                    .normalize();

// Y correction independiente — mas agresiva
            double yBlend = 0.03*( distance); // cambia este valor, mas alto = mas agresivo
            double correctedY = newDir.y + (toPlayer.y - newDir.y) * yBlend;

            this.setDeltaMovement(new Vec3(newDir.x, correctedY, newDir.z).normalize().scale(currentSpeed));
            this.acceleration = Vec3.ZERO;
        }else {
            System.out.println("mas cerca de la minima distancia") ;

        }

        super.tick();

        if (this.level() instanceof ServerLevel serverLevel && this.tickCount % 2 == 0) {
            serverLevel.sendParticles(
                    ParticleTypes.END_ROD,
                    this.getX(), this.getY(), this.getZ(),
                    1,      // count
                    0, 0, 0, // offset x y z
                    0.01    // speed
            );
        }
    }


}