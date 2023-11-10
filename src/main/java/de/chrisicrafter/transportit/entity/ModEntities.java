package de.chrisicrafter.transportit.entity;

import de.chrisicrafter.transportit.TransportIt;
import de.chrisicrafter.transportit.entity.custom.CustomMinecartFurnace;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> VANILLA_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TransportIt.MOD_ID);

    public static final RegistryObject<EntityType<CustomMinecartFurnace>> CUSTOM_MINECART_FURNACE = VANILLA_ENTITY_TYPES.register("furnace_minecart", () -> EntityType.Builder.<CustomMinecartFurnace>of(CustomMinecartFurnace::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("furnace_minecart"));

    public static void register(IEventBus eventBus) {
        eventBus.register(VANILLA_ENTITY_TYPES);
    }
}
