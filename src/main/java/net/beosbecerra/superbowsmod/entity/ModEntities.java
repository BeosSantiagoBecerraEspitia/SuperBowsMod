package net.beosbecerra.superbowsmod.entity;

import net.beosbecerra.superbowsmod.SuperBowsMod;
import net.beosbecerra.superbowsmod.entity.TrailArrow;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SuperBowsMod.MODID);

    public static final RegistryObject<EntityType<TestArrow>> TEST_ARROW =
            ENTITIES.register("test_arrow",
                    () -> EntityType.Builder
                            .<TestArrow>of(TestArrow::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("test_arrow"));

    public static final RegistryObject<EntityType<TrailArrow>> TRAIL_ARROW =
            ENTITIES.register("trail_arrow",
                    () -> EntityType.Builder
                            .<TrailArrow>of(TrailArrow::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("trail_arrow"));

    public static final RegistryObject<EntityType<HomingArrow>> HOMING_ARROW =
            ENTITIES.register("homing_arrow",
                    () -> EntityType.Builder
                            .<HomingArrow>of(HomingArrow::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("homing_arrow"));

    public static final RegistryObject<EntityType<OrbitArrow>> ORBIT_ARROW =
            ENTITIES.register("orbit_arrow",
                    () -> EntityType.Builder
                            .<OrbitArrow>of(OrbitArrow::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("orbit_arrow"));

    public static final RegistryObject<EntityType<SoyArrow>> SOY_ARROW =
            ENTITIES.register("soy_arrow",
                    () -> EntityType.Builder
                            .<SoyArrow>of(SoyArrow::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("soy_arrow"));
    public static final RegistryObject<EntityType<LaunchArrow>> LAUNCH_ARROW =
            ENTITIES.register("launch_arrow",
                    () -> EntityType.Builder
                            .<LaunchArrow>of(LaunchArrow::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("launch_arrow"));
    public static final RegistryObject<EntityType<TntArrow>> TNT_ARROW =
            ENTITIES.register("tnt_arrow",
                    () -> EntityType.Builder
                            .<TntArrow>of(TntArrow::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("tnt_arrow"));
    public static final RegistryObject<EntityType<ExpellerArrow>> EXPELLER_ARROW =
            ENTITIES.register("expeller_arrow",
                    () -> EntityType.Builder
                            .<ExpellerArrow>of(ExpellerArrow::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("expeller_arrow"));
    public static final RegistryObject<EntityType<FrozenArrow>> FROZEN_ARROW =
            ENTITIES.register("frozen_arrow",
                    () -> EntityType.Builder
                            .<FrozenArrow>of(FrozenArrow::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("frozen_arrow"));
    public static final RegistryObject<EntityType<FrozenStatueEntity>> FROZEN_STATUE =
            ENTITIES.register("frozen_statue",
                    () -> EntityType.Builder
                            .<FrozenStatueEntity>of(FrozenStatueEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("frozen_statue"));
    public static final RegistryObject<EntityType<BeeArrow>> BEE_ARROW =
            ENTITIES.register("bee_arrow",
                    () -> EntityType.Builder
                            .<BeeArrow>of(BeeArrow::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .build("bee_arrow"));
    public static final RegistryObject<EntityType<RayArrow>> RAY_ARROW =
            ENTITIES.register("ray_arrow", () ->
                    EntityType.Builder.<RayArrow>of(
                                    (type, level) -> new RayArrow(type, level),  // ← lambda explícita en vez de ::new
                                    MobCategory.MISC
                            )
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("ray_arrow")
            );
    public static final RegistryObject<EntityType<GiantArrow>> GIANT_ARROW =
            ENTITIES.register("giant_arrow",
                    () -> EntityType.Builder
                            .<GiantArrow>of(GiantArrow::new, MobCategory.MISC)
                            .sized(3.0f, 3.0f)
                            .build("giant_arrow"));

    public static void register(IEventBus eventBus) {

        ENTITIES.register(eventBus);
    }
}