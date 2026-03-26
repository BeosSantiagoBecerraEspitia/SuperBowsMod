package net.beosbecerra.superbowsmod.item;

import net.beosbecerra.superbowsmod.entity.ModEntities;
import net.beosbecerra.superbowsmod.entity.SoyArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoyBow extends BowItem {

    private static final int FIRE_RATE       = 2;
    private static final int SHOTS_PER_ARROW = 20;

    private static final Map<UUID, Integer> shotCounts = new HashMap<>();

    public SoyBow(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void onUseTick(Level level, LivingEntity shooter, ItemStack stack, int remainingUseDuration) {
        if (!(shooter instanceof Player player)) return;
        if (level.isClientSide) return;

        int ticksUsed = getUseDuration(stack) - remainingUseDuration;
        if (ticksUsed % FIRE_RATE != 0) return;

        UUID id = player.getUUID();
        int shots = shotCounts.getOrDefault(id, 0) + 1;
        shotCounts.put(id, shots);

        // consume arrow at the START of each cycle
        if (shots == 1) {
            ItemStack ammo = player.getProjectile(stack);
            if (ammo.isEmpty() && !player.getAbilities().instabuild) {
                shotCounts.remove(id);
                return;
            }
            if (!player.getAbilities().instabuild) ammo.shrink(1);
            stack.hurtAndBreak(1, shooter, (e) -> e.broadcastBreakEvent(shooter.getUsedItemHand()));
        }

        // reset counter after SHOTS_PER_ARROW
        if (shots >= SHOTS_PER_ARROW) {
            shotCounts.put(id, 0);
        }

        // spawn arrow
        SoyArrow arrow = new SoyArrow(ModEntities.SOY_ARROW.get(), level, shooter);
        arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0f, 2.0f, 2.0f);
        arrow.setEnchantmentEffectsFromEntity(shooter, 1.0f);
        arrow.pickup = Arrow.Pickup.DISALLOWED;
        level.addFreshEntity(arrow);

    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        // intentionally empty — don't reset counter on release so cycle persists between clicks
    }
}