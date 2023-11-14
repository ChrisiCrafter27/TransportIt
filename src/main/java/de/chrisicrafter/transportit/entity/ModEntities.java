package de.chrisicrafter.transportit.entity;

import de.chrisicrafter.transportit.TransportIt;
import de.chrisicrafter.transportit.entity.custom.CustomMinecart;
import de.chrisicrafter.transportit.entity.custom.CustomMinecartCommandBlock;
import de.chrisicrafter.transportit.entity.custom.CustomMinecartFurnace;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TransportIt.MOD_ID);

    public static final RegistryObject<EntityType<CustomMinecartFurnace>> CUSTOM_MINECART_FURNACE = ENTITY_TYPES.register("furnace_minecart", () -> EntityType.Builder.<CustomMinecartFurnace>of(CustomMinecartFurnace::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("furnace_minecart"));
    public static final RegistryObject<EntityType<CustomMinecartCommandBlock>> CUSTOM_MINECART_COMMAND_BLOCK = ENTITY_TYPES.register("command_block_minecart", () -> EntityType.Builder.<CustomMinecartCommandBlock>of(CustomMinecartCommandBlock::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("command_block_minecart"));
    public static final RegistryObject<EntityType<CustomMinecart>> CUSTOM_MINECART = ENTITY_TYPES.register("minecart", () -> EntityType.Builder.<CustomMinecart>of(CustomMinecart::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("minecart"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
