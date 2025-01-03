package {{ package_name }};

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
{{ #before_1_20_5 }}
import net.neoforged.fml.common.Mod;
{{ /before_1_20_5 }}
{{ #from_1_20_5 }}
import net.neoforged.fml.common.EventBusSubscriber;
{{ /from_1_20_5 }}
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
{{ #before_1_20_5 }}
@Mod.EventBusSubscriber(modid = {{ mod_class_name }}.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
{{ /before_1_20_5 }}
{{ #from_1_20_5 }}
@EventBusSubscriber(modid = {{ mod_class_name }}.MODID, bus = EventBusSubscriber.Bus.MOD)
{{ /from_1_20_5 }}
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    private static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
{{ #before_1_21 }}
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(new ResourceLocation(itemName));
{{ /before_1_21 }}
{{ #from_1_21 }}
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
{{ /from_1_21 }}
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumber = MAGIC_NUMBER.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
{{ #before_1_21_2 }}
    {{ #before_1_21 }}
                .map(itemName -> BuiltInRegistries.ITEM.get(new ResourceLocation(itemName)))
    {{ /before_1_21 }}
    {{ #from_1_21 }}
                .map(itemName -> BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemName)))
    {{ /from_1_21 }}
{{ /before_1_21_2 }}
{{ #from_1_21_2 }}
                .map(itemName -> BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(itemName)))
{{ /from_1_21_2 }}
                .collect(Collectors.toSet());
    }
}
