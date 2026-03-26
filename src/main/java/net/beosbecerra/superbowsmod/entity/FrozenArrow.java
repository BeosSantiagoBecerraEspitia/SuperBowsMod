package net.beosbecerra.superbowsmod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class FrozenArrow extends Arrow {

    private static final float LOW_HEALTH_THRESHOLD = 4.0f; // 2 hearts
    private static final int   SLOWNESS_DURATION    = 200;  // 10 seconds
    private static final int   SLOWNESS_LEVEL       = 3;    // slowness V

    public FrozenArrow(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    public FrozenArrow(EntityType<? extends Arrow> type, Level level, LivingEntity shooter) {
        super(  level, shooter);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!(result.getEntity() instanceof LivingEntity target)) {
            this.discard();
            return;
        }

        // deal normal arrow damage first
        super.onHitEntity(result);

        // apply slowness
        target.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                SLOWNESS_DURATION,
                SLOWNESS_LEVEL
        ));

        // freeze if below 2 hearts
        if (target.getHealth() <= LOW_HEALTH_THRESHOLD) {
            freeze(target);
        }
    }

    private void freeze(LivingEntity entity) {
        FrozenStatueEntity statue = FrozenStatueEntity.buildFromEntity(entity);
        statue.absMoveTo(entity.getX(), entity.getY(), entity.getZ(),
                entity.getYRot(), entity.getXRot());
        statue.yBodyRot = entity.getYRot();

        if (!level().isClientSide) {
            level().addFreshEntity(statue);
            entity.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.discard();
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}