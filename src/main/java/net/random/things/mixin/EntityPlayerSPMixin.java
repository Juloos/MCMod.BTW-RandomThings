package net.random.things.mixin;

import btw.random.things.RandomThingsAddon;
import net.minecraft.src.AbstractClientPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.MovementInput;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer {
    @Shadow public MovementInput movementInput;

    @Unique private boolean wasHoldingSpecial;

    public EntityPlayerSPMixin(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;isUsingSpecialKey()Z", ordinal = 0))
    private void storeSpecialKeyState(CallbackInfo ci) {
        this.wasHoldingSpecial = this.isUsingSpecialKey();
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;isSprinting()Z", ordinal = 2))
    private boolean redirectSprinting(EntityPlayerSP instance) {
        if (!RandomThingsAddon.toggleSprint) {
            boolean allowedToSprint = !this.doesStatusPreventSprinting() || this.capabilities.allowFlying;
            if (this.movementInput.moveForward < 0.8f || !allowedToSprint || (!this.isUsingSpecialKey() && this.wasHoldingSpecial))
                this.setSprinting(false);
            return false;
        }
        return true;
    }
}
