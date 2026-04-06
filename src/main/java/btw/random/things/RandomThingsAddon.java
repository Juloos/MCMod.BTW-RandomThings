package btw.random.things;

import java.util.Arrays;

import api.AddonHandler;
import api.BTWAddon;
import api.config.AddonConfig;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;

public class RandomThingsAddon extends BTWAddon {
    private static RandomThingsAddon instance;

    public static KeyBinding zoom_key;

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
    public static float zoomFov;
    public static Boolean precisionMode;
    public static Boolean warnDurabilityWaste;

    public static Boolean useJNativeHook = true;

    public RandomThingsAddon() {
        super();
    }

    public static RandomThingsAddon getInstance() {
        if (instance == null)
            instance = new RandomThingsAddon();
        return instance;
    }

    @Override
    public void registerConfigProperties(AddonConfig config) {
        config.registerBoolean("EnableMinecraftDateTimer", true, "Set if the minecraft date should show up or not");
        config.registerBoolean("EnableRealWorldTimer", true, "Set if the real time timer should show up or not");
        config.registerString("TimerAlignment", "Hotbar", "Places timers on some spots.\n# Allowed case-insensitive strings: \"Hotbar\", \"TopLeft\", \"Top\", \"TopRight\", \"BottomLeft\", \"BottomRight\"");
        config.registerBoolean("ToggleSprint", true, "Set whether the sprint/special key should have a toggle or hold behavior");
        config.registerInt("LanPort", 25565, "Port to always use when opening to LAN (-1 for random)");
        config.registerBoolean("AutoHealthScoreboard", true, "Set whether a health scoreboard should be automatically created when sharing to LAN or not");
        config.registerDouble("ZoomFov", -1.5, "Set the FOV value that is set when zooming in");
        config.registerBoolean("UseJNativeHook", true, "Set whether to use JNativeHook library or not (disable if mouse sensibility has acceleration issues)");
        config.registerBoolean("WarnDurabilityWaste", true, "This warns players with a sound when using more durability");
        config.registerBoolean("PrecisionMode", false, "False: Standard level precision (not suitable for speedrunning)\n# True: Highest level, shows ticks");
    }

    @Override
    public void handleConfigProperties(AddonConfig config) {
        shouldShowDateTimer = config.getBoolean("EnableMinecraftDateTimer");
        shouldShowRealTimer = config.getBoolean("EnableRealWorldTimer");
        timerAlignment = config.getString("TimerAlignment").toLowerCase();
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
        toggleSprint = config.getBoolean("ToggleSprint");
        lanPort = config.getInt("LanPort");
        autoHealthScoreboard = config.getBoolean("AutoHealthScoreboard");
        zoomFov = (float) config.getDouble("ZoomFov");
        useJNativeHook = config.getBoolean("UseJNativeHook");
        precisionMode = config.getBoolean("PrecisionMode");
        warnDurabilityWaste = config.getBoolean("WarnDurabilityWaste");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    public void initKeybind(GameSettings settings) {
        zoom_key = new KeyBinding("key.randomthings.zoom", Keyboard.KEY_F);

        f5_key = new KeyBinding("key.randomthings.perspective", Keyboard.KEY_F5);
        third_person_key = new KeyBinding("key.randomthings.thirdperson", Keyboard.KEY_X);
        first_person_key = new KeyBinding("key.randomthings.firstperson", Keyboard.KEY_Z);
        backwards_facing_key = new KeyBinding("key.randomthings.backwardsfacing", Keyboard.KEY_C);

        slot_keys = new KeyBinding[9];
        for (int i = 0; i < 9; i++)
            slot_keys[i] = new KeyBinding("key.randomthings.slot" + (i + 1), Keyboard.KEY_1 + i);

        KeyBinding[] keyBindings = settings.keyBindings;
        keyBindings = Arrays.copyOf(keyBindings, keyBindings.length + 9 + 5);
        keyBindings[keyBindings.length - 9 - 5] = zoom_key;
        keyBindings[keyBindings.length - 9 - 4] = f5_key;
        keyBindings[keyBindings.length - 9 - 3] = first_person_key;
        keyBindings[keyBindings.length - 9 - 2] = third_person_key;
        keyBindings[keyBindings.length - 9 - 1] = backwards_facing_key;
        System.arraycopy(slot_keys, 0, keyBindings, keyBindings.length - 9, 9);
        settings.keyBindings = keyBindings;
    }
}