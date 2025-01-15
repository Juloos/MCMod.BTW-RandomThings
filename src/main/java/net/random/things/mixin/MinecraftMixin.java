package net.random.things.mixin;

import net.minecraft.src.EntityClientPlayerMP;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import btw.random.things.RandomThingsAddon;
import net.minecraft.src.Minecraft;
import net.minecraft.src.GameSettings;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    public GameSettings gameSettings;

    @Shadow
    public EntityClientPlayerMP thePlayer;

    @Inject(method = "startGame", at = @At("RETURN"))
    private void startGame(CallbackInfo ci) {
        RandomThingsAddon.getInstance().initKeybind();
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 13))
    private int redirectF5Call() {
        if (RandomThingsAddon.first_person_key.isPressed())
            gameSettings.thirdPersonView = 0;
        else if (RandomThingsAddon.third_person_key.isPressed())
            gameSettings.thirdPersonView = 1;
        else if (RandomThingsAddon.backwards_facing_key.isPressed())
            gameSettings.thirdPersonView = 2;
        else if (RandomThingsAddon.f5_key.isPressed())
            return Keyboard.KEY_F5;
        return -1;
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 15))
    private int removeVanillaSlotCall() {
        return -1;
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 15, shift = At.Shift.BY, by = -9))
    private void handleSlotKeys(CallbackInfo ci) {
        for (int i = 0; i < RandomThingsAddon.slot_keys.length; i++)
            if (RandomThingsAddon.slot_keys[i].isPressed())
                this.thePlayer.inventory.currentItem = i;
    }
}
