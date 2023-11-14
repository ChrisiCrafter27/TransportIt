package de.chrisicrafter.transportit.item;

import de.chrisicrafter.transportit.TransportIt;
import de.chrisicrafter.transportit.entity.custom.CustomAbstractMinecart;
import de.chrisicrafter.transportit.item.custom.CustomMinecartItem;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TransportIt.MOD_ID);
    public static final DeferredRegister<Item> VANILLA_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public static final RegistryObject<Item> CUSTOM_FURNACE_MINECART = VANILLA_ITEMS.register("furnace_minecart", () -> new CustomMinecartItem(CustomAbstractMinecart.Type.FURNACE, (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> CUSTOM_COMMAND_BLOCK_MINECART = VANILLA_ITEMS.register("command_block_minecart", () -> new CustomMinecartItem(CustomAbstractMinecart.Type.COMMAND_BLOCK, (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> CUSTOM_MINECART = VANILLA_ITEMS.register("minecart", () -> new CustomMinecartItem(CustomAbstractMinecart.Type.RIDEABLE, (new Item.Properties()).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        VANILLA_ITEMS.register(eventBus);
    }
}
