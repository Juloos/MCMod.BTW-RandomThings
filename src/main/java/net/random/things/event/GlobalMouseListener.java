package net.random.things.event;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import net.minecraft.src.Minecraft;
import org.lwjgl.input.Mouse;

public class GlobalMouseListener implements NativeMouseInputListener {
    private final boolean[] buttonsStates;

    public GlobalMouseListener(int buttonsCount) {
        this.buttonsStates = new boolean[buttonsCount];
    }

    public void nativeMouseClicked(NativeMouseEvent e) {
        // ignored
    }

    public void nativeMousePressed(NativeMouseEvent e) {
        if (Minecraft.getMinecraft() == null || !Minecraft.getMinecraft().inGameHasFocus)
            return;
        if (0 < e.getButton() && e.getButton() <= buttonsStates.length)
            buttonsStates[e.getButton() - 1] = true;
    }

    public void nativeMouseReleased(NativeMouseEvent e) {
        if (Minecraft.getMinecraft() == null || !Minecraft.getMinecraft().inGameHasFocus)
            return;
        if (0 < e.getButton() && e.getButton() <= buttonsStates.length)
            buttonsStates[e.getButton() - 1] = false;
    }

    public void nativeMouseMoved(NativeMouseEvent e) {
        // ignored
    }

    public void nativeMouseDragged(NativeMouseEvent e) {
        // ignored
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
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
            return;
        }

        instance = listener;
        GlobalScreen.addNativeMouseListener(listener);
        GlobalScreen.addNativeMouseMotionListener(listener);
    }
}
