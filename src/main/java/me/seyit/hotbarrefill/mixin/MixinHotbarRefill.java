package me.seyit.hotbarrefill.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;
import me.seyit.hotbarrefill.HotbarRefill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinHotbarRefill {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen instanceof InventoryScreen) {
            if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_3) == GLFW.GLFW_PRESS) {
                Item selectedItem = Registries.ITEM.get(new Identifier(HotbarRefill.selectedItem));
                int itemInInventory = findItemInInventory(mc, selectedItem, 9, 36);
                if (itemInInventory != -1) {
                    int emptyHotbarSlot = findEmptyHotbarSlot(mc);
                    if (emptyHotbarSlot != -1) {
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, itemInInventory, emptyHotbarSlot - 36, SlotActionType.SWAP, mc.player);
                    }
                }
            }
        }
    }

    private int findItemInInventory(MinecraftClient mc, Item item, int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    private int findEmptyHotbarSlot(MinecraftClient mc) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isEmpty()) {
                return i + 36;
            }
        }
        return -1;
    }
}
