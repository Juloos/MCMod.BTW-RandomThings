package net.random.things.mixin;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.KeyBinding;
import net.random.things.event.GlobalMouseListener;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import btw.random.things.RandomThingsAddon;
import net.minecraft.src.Minecraft;
import net.minecraft.src.GameSettings;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    public GameSettings gameSettings;

    @Shadow
    public EntityClientPlayerMP thePlayer;

    @Unique private float oldFovSetting;
    @Unique private boolean wasZooming = false;

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 13))
    private int removeVanillaF5Call() {
        return -1;
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 15))
    private int removeVanillaSlotCall() {
        return -1;
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "net/minecraft/src/GameSettings.chatVisibility : I"))
    private void handleSlotKeys(CallbackInfo ci) {
        // Perspective keybindings
        if (RandomThingsAddon.first_person_key.isPressed())
            gameSettings.thirdPersonView = 0;
        else if (RandomThingsAddon.third_person_key.isPressed())
            gameSettings.thirdPersonView = 1;
        else if (RandomThingsAddon.backwards_facing_key.isPressed())
            gameSettings.thirdPersonView = 2;
        else if (RandomThingsAddon.f5_key.isPressed())
            gameSettings.thirdPersonView = (gameSettings.thirdPersonView + 1) % 3;

        // Slots keybindings
        for (int i = 0; i < RandomThingsAddon.slot_keys.length; i++)
            if (RandomThingsAddon.slot_keys[i].isPressed())
                this.thePlayer.inventory.currentItem = i;

        // Zoom keybinding
        if (RandomThingsAddon.zoom_key.pressed && !wasZooming) {
            oldFovSetting = this.gameSettings.fovSetting;
            this.gameSettings.fovSetting = RandomThingsAddon.zoomFov;
            this.gameSettings.smoothCamera = true;
            wasZooming = true;
        }
        if (!RandomThingsAddon.zoom_key.pressed && wasZooming) {
            this.gameSettings.fovSetting = oldFovSetting;
            this.gameSettings.smoothCamera = false;
            wasZooming = false;
        }
    }

    @Redirect(
            method = "runTick",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;getEventButton()I")
    )
    private int removeMouseButtonShift() {
        if (Mouse.getEventButton() > 2 && GlobalMouseListener.getInstance() != null)
            return -4269;
        return Mouse.getEventButton();
    }

    @Inject(
            method = "runTick",
            at = @At(value = "FIELD", target = "Lnet/minecraft/src/Minecraft;leftClickCounter:I", ordinal = 1)
    )
    private void fixMouseButtonShift(CallbackInfo ci) {
        boolean pressed;
        for (int i = 3; i < Mouse.getButtonCount(); i++) {
            pressed = GlobalMouseListener.isButtonDown(i);
            KeyBinding.setKeyBindState(i - 100, pressed);
            if (pressed)
                KeyBinding.onTick(i - 100);
        }
    }
}
