package net.beosbecerra.superbowsmod.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class TestArrow extends ModArrow {

    public TestArrow(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.baseDamage  = 5.0f;
        this.maxDistance = 30.0;
    }

    public TestArrow(EntityType<? extends AbstractArrow> type, Level level, LivingEntity shooter) {
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
}