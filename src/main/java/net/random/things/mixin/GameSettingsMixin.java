package net.random.things.mixin;

import btw.random.things.RandomThingsAddon;
import net.minecraft.src.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSettings.class)
public abstract class GameSettingsMixin {
    @Inject(
            method = "<init>(Lnet/minecraft/src/Minecraft;Ljava/io/File;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GameSettings;loadOptions()V")
    )
    private void initMixin(CallbackInfo ci) {
        RandomThingsAddon.getInstance().initKeybind((GameSettings) (Object) this);
    }
}
