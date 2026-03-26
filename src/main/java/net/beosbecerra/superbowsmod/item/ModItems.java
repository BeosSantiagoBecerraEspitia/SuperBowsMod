package net.beosbecerra.superbowsmod.item;

import net.beosbecerra.superbowsmod.SuperBowsMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SuperBowsMod.MODID);



    public static final RegistryObject<Item> TRAIL_BOW =
            ITEMS.register("trail_bow",()-> new TrailBow(new Item.Properties().stacksTo(1).durability(400)));

    public static final RegistryObject<Item> TEST_BOW =
            ITEMS.register("test_bow", () -> new TestBow(new Item.Properties().stacksTo(1).durability(400)));

    public static final RegistryObject<Item> HOMING_BOW =
            ITEMS.register("homing_bow", () -> new HomingBow(new Item.Properties().stacksTo(1).durability(400)));

    public static final RegistryObject<Item> ORBIT_BOW =
            ITEMS.register("orbit_bow", () -> new OrbitBow(new Item.Properties().stacksTo(1).durability(400)));
    public static final RegistryObject<Item> SOY_BOW =
            ITEMS.register("soy_bow", () -> new SoyBow(new Item.Properties().stacksTo(1).durability(400)));
    public static final RegistryObject<Item> GOAT_BOW =
            ITEMS.register("goat_bow", () -> new GoatBow(new Item.Properties().stacksTo(1).durability(400)));
    public static final RegistryObject<Item> LAUNCH_BOW =
            ITEMS.register("launch_bow", () -> new LaunchBow(new Item.Properties().stacksTo(1).durability(400)));
    public static final RegistryObject<Item> TNT_BOW =
            ITEMS.register("tnt_bow", () -> new TntBow(new Item.Properties().stacksTo(1).durability(400)));
    public static final RegistryObject<Item> EXPELLER_BOW =
            ITEMS.register("expeller_bow", () -> new ExpellerBow(new Item.Properties().stacksTo(1).durability(1200)));
    public static final RegistryObject<Item> FROZEN_BOW =
            ITEMS.register("frozen_bow", () -> new FrozenBow(new Item.Properties().stacksTo(1).durability(400)));
    public static final RegistryObject<Item> BEE_BOW =
            ITEMS.register("bee_bow", () -> new BeeBow(new Item.Properties().stacksTo(1).durability(400)));
    public static final RegistryObject<Item> CLUSTER_BOW =
            ITEMS.register("cluster_bow", () -> new ClusterBow(new Item.Properties().stacksTo(1).durability(400)));
    public static final RegistryObject<Item> RAY_BOW =
            ITEMS.register("ray_bow", () -> new RayBow(new Item.Properties().stacksTo(1).durability(400)));
    public static final RegistryObject<Item> GIANT_BOW =
            ITEMS.register("giant_bow", () -> new GiantBow(new Item.Properties().stacksTo(1).durability(400)));
    public static final RegistryObject<Item> UPGRADE = ITEMS.register("upgrade",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SUPERMAGICCRYSTAL = ITEMS.register("super_magic_crystal",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register((eventBus));
    }
 }
