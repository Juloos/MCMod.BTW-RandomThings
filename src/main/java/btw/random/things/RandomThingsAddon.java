package btw.random.things;

import java.util.Arrays;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import btw.AddonHandler;
import btw.BTWAddon;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;

public class RandomThingsAddon extends BTWAddon {
    private static RandomThingsAddon instance;

    public static KeyBinding f5_key;
    public static KeyBinding third_person_key;
    public static KeyBinding first_person_key;
    public static KeyBinding backwards_facing_key;

    public static KeyBinding[] slot_keys;

    public static Boolean shouldShowDateTimer;
    public static Boolean shouldShowRealTimer;
    public static String timerAlignment;
    public static Boolean toggleSprint;
    public static int lanPort;
    public static Boolean autoHealthScoreboard;

    public RandomThingsAddon() {
        super();
    }

    public static RandomThingsAddon getInstance() {
        if (instance == null)
            instance = new RandomThingsAddon();
        return instance;
    }

    @Override
    public void preInitialize() {
        this.registerProperty("EnableMinecraftDateTimer", "True", "Set if the minecraft date should show up or not");
        this.registerProperty("EnableRealWorldTimer", "True", "Set if the real time timer should show up or not");
        this.registerProperty("TimerAlignment", "Hotbar", "Places timers on some spots.\n# Allowed case-insensitive strings: \"Hotbar\", \"TopLeft\", \"Top\", \"TopRight\", \"BottomLeft\", \"BottomRight\"");
        this.registerProperty("ToggleSprint", "True", "Set whether the sprint/special key should have a toggle or hold behavior");
        this.registerProperty("LanPort", "25565", "Port to always use when opening to LAN (-1 for random)");
        this.registerProperty("AutoHealthScoreboard", "True", "Set whether a health scoreboard should be automatically created when sharing to LAN or not");
    }

    @Override
    public void handleConfigProperties(Map<String, String> propertyValues) {
        shouldShowDateTimer = Boolean.parseBoolean(propertyValues.get("EnableMinecraftDateTimer"));
        shouldShowRealTimer = Boolean.parseBoolean(propertyValues.get("EnableRealWorldTimer"));
        timerAlignment = propertyValues.get("TimerAlignment").toLowerCase();
        switch (timerAlignment) {
            case "topleft":
            case "topright":
            case "top":
            case "bottomleft":
            case "bottomright":
            case "hotbar":
                break;
            default:
                timerAlignment = "hotbar";
        }
        toggleSprint = Boolean.parseBoolean(propertyValues.get("ToggleSprint"));
        lanPort = Integer.parseInt(propertyValues.get("LanPort"));
        autoHealthScoreboard = Boolean.parseBoolean(propertyValues.get("AutoHealthScoreboard"));
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    public void initKeybind(GameSettings settings) {
        f5_key = new KeyBinding("key.randomthings.perspective", Keyboard.KEY_F5);
        third_person_key = new KeyBinding("key.randomthings.thirdperson", Keyboard.KEY_X);
        first_person_key = new KeyBinding("key.randomthings.firstperson", Keyboard.KEY_Z);
        backwards_facing_key = new KeyBinding("key.randomthings.backwardsfacing", Keyboard.KEY_C);

        slot_keys = new KeyBinding[9];
        for (int i = 0; i < 9; i++)
            slot_keys[i] = new KeyBinding("key.randomthings.slot" + (i + 1), Keyboard.KEY_1 + i);

        KeyBinding[] keyBindings = settings.keyBindings;
        keyBindings = Arrays.copyOf(keyBindings, keyBindings.length + 9 + 4);
        keyBindings[keyBindings.length - 9 - 4] = f5_key;
        keyBindings[keyBindings.length - 9 - 3] = first_person_key;
        keyBindings[keyBindings.length - 9 - 2] = third_person_key;
        keyBindings[keyBindings.length - 9 - 1] = backwards_facing_key;
        System.arraycopy(slot_keys, 0, keyBindings, keyBindings.length - 9, 9);
        settings.keyBindings = keyBindings;
    }
}