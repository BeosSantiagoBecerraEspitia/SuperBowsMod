package net.beosbecerra.superbowsmod.item;

import net.beosbecerra.superbowsmod.entity.ModEntities;
import net.beosbecerra.superbowsmod.entity.RayArrow;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;

public class RayBow extends BowItem {

    public RayBow(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        // Require at least a minimal charge (10 ticks) to fire
        float charge = BowItem.getPowerForTime(this.getUseDuration(stack) - timeLeft);
        if (charge < 1.0f) return;

        // Require an arrow in inventory (or creative mode)
        if (shooter.getProjectile(stack).isEmpty()) return;


        if (!level.isClientSide()) {
            RayArrow ray = new RayArrow(ModEntities.RAY_ARROW.get(), level, shooter);

            // Shoot from the shooter's eye position, full forward direction
            ray.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0f, 1.0f, 0.0f);

            // Freeze the ray in place immediately after setting its rotation/position
            // (velocity is only used in tick() for direction reconstruction; we zero it after tick 1)
            ray.yRotO = ray.getYRot();
            ray.xRotO = ray.getXRot();

            level.addFreshEntity(ray);

            // Consume one arrow (unless creative)
            if (!(shooter instanceof Player player) || !player.getAbilities().instabuild) {
                shooter.getProjectile(stack).shrink(1);
            }
            stack.hurtAndBreak(1, shooter, (e) -> e.broadcastBreakEvent(shooter.getUsedItemHand()));
        }

        // Satisfying zap sound
        level.playSound(
                null, shooter.getX(), shooter.getY(), shooter.getZ(),
                SoundEvents.TRIDENT_THUNDER,
                SoundSource.PLAYERS,
                0.8f, 1.5f
        );
    }
}