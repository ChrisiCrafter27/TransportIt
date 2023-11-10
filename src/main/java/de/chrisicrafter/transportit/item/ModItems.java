package de.chrisicrafter.transportit.item;

import de.chrisicrafter.transportit.item.custom.CustomMinecartItem;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> VANILLA_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public static final RegistryObject<Item> CUSTOM_FURNACE_MINECART = VANILLA_ITEMS.register("furnace_minecart", () -> new CustomMinecartItem(AbstractMinecart.Type.FURNACE, (new Item.Properties()).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        VANILLA_ITEMS.register(eventBus);
    }
}
