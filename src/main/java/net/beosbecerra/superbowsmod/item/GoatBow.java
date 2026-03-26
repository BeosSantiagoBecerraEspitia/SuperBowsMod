package net.beosbecerra.superbowsmod.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GoatBow extends BowItem {

    private static final int KNOCKBACK_STRENGTH = 10; // vanilla max is 2, we go way higher

    public GoatBow(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        if (!(shooter instanceof Player player)) return;

        ItemStack ammo = player.getProjectile(stack);
        if (ammo.isEmpty() && !player.getAbilities().instabuild) return;

        float charge = BowItem.getPowerForTime(this.getUseDuration(stack) - timeLeft);
        if (charge < 0.1f) return;

        if (!level.isClientSide) {
            Arrow arrow = new Arrow(level, shooter);
            arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0f, charge * 3.0f, 1.0f);
            arrow.setEnchantmentEffectsFromEntity(shooter, charge);
            arrow.setKnockback(KNOCKBACK_STRENGTH);
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            level.addFreshEntity(arrow);

            if (!player.getAbilities().instabuild) ammo.shrink(1);
            stack.hurtAndBreak(1, shooter, (e) -> e.broadcastBreakEvent(shooter.getUsedItemHand()));
        }
    }
}