package net.beosbecerra.superbowsmod.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ClusterBow extends BowItem {

    private static final int   ARROWS_PER_SHOT = 20;
    private static final float ARROW_DAMAGE     = 12.0f;
    private static final float SPEED            = 3.0f;
    private static final float INACCURACY       = 30.0f; // spread between arrows

    public ClusterBow(Item.Properties properties) {
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
            for (int i = 0; i < ARROWS_PER_SHOT; i++) {
                Arrow arrow = new Arrow(level, shooter);
                arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0f, SPEED, INACCURACY);
                arrow.setEnchantmentEffectsFromEntity(shooter, charge);
                arrow.setBaseDamage(ARROW_DAMAGE);
                arrow.pickup = Arrow.Pickup.DISALLOWED;
                level.addFreshEntity(arrow);
            }

            if (!player.getAbilities().instabuild) {
                ammo.shrink(1);
            }
            stack.hurtAndBreak(1, shooter, (e) -> e.broadcastBreakEvent(shooter.getUsedItemHand()));
        }
    }
}