package net.random.things.event;

import btw.random.things.RandomThingsAddon;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import net.minecraft.src.Minecraft;
import org.lwjgl.input.Mouse;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalMouseListener implements NativeMouseInputListener {
    private final boolean[] buttonsStates;

    public GlobalMouseListener(int buttonsCount) {
        this.buttonsStates = new boolean[buttonsCount];
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        if (Minecraft.getMinecraft() == null || !Minecraft.getMinecraft().inGameHasFocus)
            return;
        if (0 < e.getButton() && e.getButton() <= buttonsStates.length)
            buttonsStates[e.getButton() - 1] = true;
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        if (Minecraft.getMinecraft() == null || !Minecraft.getMinecraft().inGameHasFocus)
            return;
        if (0 < e.getButton() && e.getButton() <= buttonsStates.length)
            buttonsStates[e.getButton() - 1] = false;
    }

    public static boolean isButtonDown(int button) {
        return getInstance().buttonsStates[button];
    }

    private static GlobalMouseListener instance;

    public static GlobalMouseListener getInstance() {
        if (instance == null)
            setup(new GlobalMouseListener(Mouse.getButtonCount()));
        return instance;
    }

    private static void setup(GlobalMouseListener listener) {
        if (!RandomThingsAddon.useJNativeHook)
            return;

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
            return;
        }

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        instance = listener;
        GlobalScreen.addNativeMouseListener(listener);
    }
}
