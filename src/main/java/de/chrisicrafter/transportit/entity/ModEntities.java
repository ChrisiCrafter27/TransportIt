package de.chrisicrafter.transportit.entity;

import de.chrisicrafter.transportit.entity.custom.CustomMinecartFurnace;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> VANILLA_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "minecraft");

    public static final RegistryObject<EntityType<CustomMinecartFurnace>> CUSTOM_MINECART_FURNACE = VANILLA_ENTITY_TYPES.register("furnace_minecart", () -> EntityType.Builder.<CustomMinecartFurnace>of(CustomMinecartFurnace::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build(/*This value seems to be ignored*/"furnace_minecart"));

    public static void register(IEventBus eventBus) {
        VANILLA_ENTITY_TYPES.register(eventBus);
    }
}
