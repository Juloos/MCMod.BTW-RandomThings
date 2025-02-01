package net.random.things.mixin;

import btw.random.things.RandomThingsAddon;
import net.minecraft.src.HttpUtil;
import net.minecraft.src.IntegratedServerListenThread;
import net.minecraft.src.ServerListenThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IntegratedServerListenThread.class)
public abstract class IntegratedServerListenThreadMixin {
    @Redirect(
            method = "func_71755_c",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ServerListenThread;start()V")
    )
    private void redirectServerListenThread(ServerListenThread instance) {
        int port = RandomThingsAddon.lanPort;
        if (port <= 0)
            port = HttpUtil.func_76181_a();
        if (port <= 0)
            port = 25564;  // Default port in the code, for some reason its not 25565
        instance.myPort = port;
    }
}
