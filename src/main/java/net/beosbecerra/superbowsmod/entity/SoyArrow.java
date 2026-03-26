package net.beosbecerra.superbowsmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

public class SoyArrow extends Arrow {

    private static final float FIXED_DAMAGE = 0.5f; // half heart

    public SoyArrow(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    public SoyArrow(EntityType<? extends Arrow> type, Level level, LivingEntity shooter) {
        super(level, shooter);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        var entity = result.getEntity();

        // apply flame
        if (this.isOnFire()) {
            entity.setSecondsOnFire(5);
        }

        //   apply knockback
        if (this.getKnockback() > 0) {
            Vec3 knockbackDir = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(this.getKnockback() * 0.6);
            if (knockbackDir.lengthSqr() > 0.0) {
                entity.push(knockbackDir.x, 0.1, knockbackDir.z);
            }
        }

        // fixed damage + power bonus
        int powerLevel = 0;
        if (this.getOwner() instanceof LivingEntity livingOwner) {
            powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, livingOwner);
        }
        float finalDamage = FIXED_DAMAGE + (powerLevel * 0.5f);

        entity.hurt(
                this.damageSources().arrow(this, this.getOwner()),
                finalDamage
        );

        this.discard();
    }
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}