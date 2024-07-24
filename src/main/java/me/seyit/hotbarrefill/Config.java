package me.seyit.hotbarrefill;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.text.Text;
import net.minecraft.registry.Registries;

import java.util.List;
import java.util.stream.Collectors;

public class Config implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Hotbar Refill Config"));

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startTextDescription(Text.literal("Hotbar Refill Configuration")).build());

            List<String> itemNames = Registries.ITEM.stream()
                    .map(item -> Registries.ITEM.getId(item).toString())
                    .sorted()
                    .collect(Collectors.toList());

            DropdownMenuBuilder<String> itemDropdownMenu = entryBuilder
                    .startDropdownMenu(Text.literal("Select Item"), DropdownMenuBuilder.TopCellElementBuilder.of(HotbarRefill.selectedItem, name -> name))
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
