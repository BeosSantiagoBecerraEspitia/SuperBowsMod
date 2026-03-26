package net.beosbecerra.superbowsmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class LaunchArrow extends Arrow {

    private float launchPower = 2.0f; // default

    public void setLaunchPower(float power) {
        this.launchPower = power;
    }

    public LaunchArrow(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    public LaunchArrow(EntityType<? extends Arrow> type, Level level, LivingEntity shooter) {
        super(level, shooter);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        var entity = result.getEntity();

        // deal normal damage first
        super.onHitEntity(result);

        // launch straight up — override Y velocity completely
        Vec3 current = entity.getDeltaMovement();
        entity.setDeltaMovement(current.x * 0.2, launchPower, current.z * 0.2);
        entity.hurtMarked = true; // tells client to sync the new velocity immediately
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}