package me.seyit.hotbarrefill;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class HotbarRefill implements ClientModInitializer {
    public static String selectedItem = "minecraft:splash_potion";

    @Override
    public void onInitializeClient() {
        Config.init();
        ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);
    }

    private void handleClientTick(Minecraft client) {
        if (client.player == null || client.gameMode == null) {
            return;
        }

        if (!(client.gui.screen() instanceof InventoryScreen)) {
            return;
        }

        if (GLFW.glfwGetMouseButton(client.getWindow().handle(), GLFW.GLFW_MOUSE_BUTTON_3) != GLFW.GLFW_PRESS) {
            return;
        }

        Identifier itemId = Identifier.tryParse(selectedItem);
        if (itemId == null) {
            return;
        }

        Item item = BuiltInRegistries.ITEM.getOptional(itemId).orElse(null);
        if (item == null) {
            return;
        }

        int itemInInventory = findItemInInventory(client, item, 9, 36);
        if (itemInInventory == -1) {
            return;
        }

        int emptyHotbarSlot = findEmptyHotbarSlot(client);
        if (emptyHotbarSlot == -1) {
            return;
        }

        client.gameMode.handleContainerInput(
                client.player.containerMenu.containerId,
                itemInInventory,
                emptyHotbarSlot - 36,
                ContainerInput.SWAP,
                client.player
        );
    }

    private int findItemInInventory(Minecraft client, Item item, int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stack = client.player.getInventory().getItem(i);
            if (stack.getItem() == item) {
                return i;
            }
        }

        return -1;
    }

    private int findEmptyHotbarSlot(Minecraft client) {
        for (int i = 0; i < 9; i++) {
            if (client.player.getInventory().getItem(i).isEmpty()) {
                return i + 36;
            }
        }

        return -1;
    }
}
