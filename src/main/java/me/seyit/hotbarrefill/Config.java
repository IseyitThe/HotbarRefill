package me.seyit.hotbarrefill;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.stream.Collectors;

public class Config implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal("Hotbar Refill Config"));

            ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startTextDescription(Component.literal("Hotbar Refill Configuration")).build());

            List<String> itemNames = BuiltInRegistries.ITEM.stream()
                    .map(item -> BuiltInRegistries.ITEM.getKey(item).toString())
                    .sorted()
                    .collect(Collectors.toList());

            DropdownMenuBuilder<String> itemDropdownMenu = entryBuilder
                    .startDropdownMenu(Component.literal("Select Item"), DropdownMenuBuilder.TopCellElementBuilder.of(HotbarRefill.selectedItem, name -> name))
                    .setDefaultValue("minecraft:splash_potion")
                    .setSelections(itemNames)
                    .setSuggestionMode(false)
                    .setSaveConsumer(newValue -> HotbarRefill.selectedItem = newValue);

            general.addEntry(itemDropdownMenu.build());

            builder.setSavingRunnable(() -> {
            });

            return builder.build();
        };
    }

    public static void init() {
    }

}
