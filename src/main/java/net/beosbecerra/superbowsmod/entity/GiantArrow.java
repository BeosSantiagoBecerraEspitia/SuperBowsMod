package net.beosbecerra.superbowsmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class GiantArrow extends ModArrow {

    public GiantArrow(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.baseDamage  = 20.0f;
        this.maxDistance = Double.MAX_VALUE;
        this.maxAge      = 400; // 20 seconds
    }

    public GiantArrow(EntityType<? extends AbstractArrow> type, Level level, LivingEntity shooter) {
        super(type, level, shooter);
        this.baseDamage  = 20.0f;
        this.maxDistance = Double.MAX_VALUE;
        this.maxAge      = 400;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        applyEnchantmentEffects(result.getEntity());
        result.getEntity().hurt(
                this.damageSources().thrown(this, this.getOwner()),
                baseDamage
        );
       // this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Vec3 velocity = this.getDeltaMovement();

        // reflect velocity based on which face was hit
        switch (result.getDirection()) {
            case UP, DOWN       -> this.setDeltaMovement(velocity.x, -velocity.y, velocity.z);
            case NORTH, SOUTH   -> this.setDeltaMovement(velocity.x, velocity.y, -velocity.z);
            case EAST, WEST     -> this.setDeltaMovement(-velocity.x, velocity.y, velocity.z);
        }

        // nudge arrow away from the block surface to prevent getting stuck
        Vec3 nudge = Vec3.atLowerCornerOf(result.getDirection().getNormal()).scale(0.1);
        this.setPos(this.getX() + nudge.x, this.getY() + nudge.y, this.getZ() + nudge.z);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}