package net.beosbecerra.superbowsmod.item;

import net.beosbecerra.superbowsmod.entity.ModEntities;
import net.beosbecerra.superbowsmod.entity.TestArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TestBow extends BowItem {

    private static final float ARROW_SPEED = 0.5f;

    public TestBow(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {

        if (shooter.getProjectile(stack).isEmpty()) return;

        TestArrow arrow = new TestArrow(ModEntities.TEST_ARROW.get(), level, shooter);

        arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0f, ARROW_SPEED, 0.0f);

        // NOW the rotation is set correctly, sync the old rotation fields
        arrow.yRotO = arrow.getYRot();
        arrow.xRotO = arrow.getXRot();

        level.addFreshEntity(arrow);

        shooter.getProjectile(stack).shrink(1);
    }
}