package net.random.things.mixin;

import btw.random.things.RandomThingsAddon;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiContainer.class)
public abstract class GuiContainerMixin extends GuiScreen {
    @Shadow protected abstract void handleMouseClick(Slot par1Slot, int par2, int par3, int par4);

    @Shadow private Slot theSlot;

    @Shadow protected abstract boolean checkHotbarKeys(int par1);

    @Inject(
            method = "checkHotbarKeys",
            at = @At("HEAD"),
            cancellable = true
    )
    private void checkHotbarKeys(int key, CallbackInfoReturnable<Boolean> cir) {
        if (this.mc.thePlayer.inventory.getItemStack() == null && this.theSlot != null) {
            for (int i = 0; i < 9; i++) {
                if (key == RandomThingsAddon.slot_keys[i].keyCode) {
                    this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, i, 2);
                    cir.setReturnValue(true);
                }
            }
        }
        cir.setReturnValue(false);
    }

    @Inject(
            method = "mouseClicked",
            at = @At("TAIL")
    )
    private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        this.checkHotbarKeys(mouseButton - 100);
    }
}
