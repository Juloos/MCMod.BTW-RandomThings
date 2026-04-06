package net.random.things.mixin;

import btw.random.things.RandomThingsAddon;
import net.minecraft.src.IntegratedServerListenThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(IntegratedServerListenThread.class)
public abstract class IntegratedServerListenThreadMixin {
    @ModifyArg(
            method = "func_71755_c",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ServerListenThread;<init>(Lnet/minecraft/src/NetworkListenThread;Ljava/net/InetAddress;I)V"),
            index = 2
    )
    private int redirectServerListenThread(int port) {
        return RandomThingsAddon.lanPort > 0 ? RandomThingsAddon.lanPort : port;
    }
}
