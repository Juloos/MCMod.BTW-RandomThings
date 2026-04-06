package net.random.things.mixin;

import btw.random.things.RandomThingsAddon;
import net.minecraft.src.AbstractClientPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.MovementInput;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer {
    @Shadow public MovementInput movementInput;

    @Shadow public abstract boolean isUsingSpecialKey();

    public EntityPlayerSPMixin(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;isSprinting()Z", ordinal = 2))
    private boolean redirectSprinting(EntityPlayerSP instance) {
        if (!RandomThingsAddon.toggleSprint) {
            boolean allowedToSprint = !this.doesStatusPreventSprinting() || this.capabilities.allowFlying;
            this.setSprinting(this.isUsingSpecialKey() && allowedToSprint && this.movementInput.moveForward >= 0.8f);
            return false;
        }
        return isSprinting();
    }
}
