package net.beosbecerra.superbowsmod.item;

import net.beosbecerra.superbowsmod.entity.TrailArrow;
import net.beosbecerra.superbowsmod.item.ModItems;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TrailBow extends BowItem {

    public TrailBow(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {

        if (shooter.getProjectile(stack).isEmpty()){
            return;
        }

        if (!level.isClientSide) {

            TrailArrow arrow = new TrailArrow(level, shooter);

            arrow.shootFromRotation(
                    shooter,
                    shooter.getXRot(),
                    shooter.getYRot(),
                    0.0F,
                    3.0F,
                    1.0F
            );

            level.addFreshEntity(arrow);

            ItemStack ammo = shooter.getProjectile(stack);
            ammo.shrink(1);
            stack.hurtAndBreak(1, shooter, (e) -> e.broadcastBreakEvent(shooter.getUsedItemHand()));
        }
    }
}