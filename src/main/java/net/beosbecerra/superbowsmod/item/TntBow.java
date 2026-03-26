package net.beosbecerra.superbowsmod.item;

import net.beosbecerra.superbowsmod.entity.ModEntities;
import net.beosbecerra.superbowsmod.entity.TntArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TntBow extends BowItem {

    public TntBow(Item.Properties properties) {
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
            TntArrow arrow = new TntArrow(ModEntities.TNT_ARROW.get(), level, shooter);
            arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0f, charge * 3.0f, 1.0f);
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            level.addFreshEntity(arrow);

            if (!player.getAbilities().instabuild) ammo.shrink(1);
            stack.hurtAndBreak(1, shooter, (e) -> e.broadcastBreakEvent(shooter.getUsedItemHand()));
        }
    }
}