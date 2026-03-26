package net.beosbecerra.superbowsmod.item;

import net.beosbecerra.superbowsmod.entity.ModEntities;
import net.beosbecerra.superbowsmod.entity.OrbitArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OrbitBow extends BowItem {

    private static final float ARROW_SPEED = 0.5f;

    public OrbitBow(Item.Properties properties) {
        super(properties);

    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        if (shooter.getProjectile(stack).isEmpty()) return;

        float charge = BowItem.getPowerForTime(this.getUseDuration(stack) - timeLeft);
        if (charge < 0.1f) return; // minimum charge required

        OrbitArrow arrow = new OrbitArrow(ModEntities.ORBIT_ARROW.get(), level, shooter);


        // speed and damage scale with charge
        arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0f, charge * 2.0f, 0.0f);
        arrow.setBaseDamage(3.0f + charge * 8.0f);
        arrow.setEnchantmentEffectsFromEntity(shooter, charge);
        System.out.println("base damage after enchant: " + arrow.getBaseDamage());
        System.out.println("on fire: " + arrow.isOnFire());

        level.addFreshEntity(arrow);
        shooter.getProjectile(stack).shrink(1);
        stack.hurtAndBreak(1, shooter, (e) -> e.broadcastBreakEvent(shooter.getUsedItemHand()));
    }
}