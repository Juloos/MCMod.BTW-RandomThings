package net.random.things.mixin;

import net.minecraft.src.EntityRenderer;
import net.minecraft.src.MouseFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Shadow private MouseFilter mouseFilterXAxis;

    @Shadow private MouseFilter mouseFilterYAxis;

    @Inject(
            method = "updateCameraAndRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/EntityClientPlayerMP;setAngles(FF)V",
                    ordinal = 1
            )
    )
    private void onRenderWorld(float par1, CallbackInfo ci) {
        this.mouseFilterXAxis = new MouseFilter();
        this.mouseFilterYAxis = new MouseFilter();
    }
}
