package net.random.things.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiControls.class)
public abstract class GuiControlsMixin extends GuiScreen {
    @Shadow
    private GameSettings options;

    @Shadow
    protected String screenTitle;

    @Shadow
    private int buttonId;

    @Unique
    final private int buttonXMargin = 6;
    @Unique
    final private int buttonYMargin = 2;

    /**
     * @author Juloos
     * @reason Bad height positioning logic
     */
    @Overwrite
    public void initGui() {
        int xCenter = this.width / 2;
        int rowSpan = (this.width - 4 * this.buttonXMargin - 40) / 3;
        int yMargin = 40;  // screenTitle is set at height 20, hardcoded
        int buttonWidth = 70;
        int buttonHeight = 20;
        int totalButtons = this.options.keyBindings.length;
        int verticalButtons = Math.floorDiv(totalButtons, 3) + 1;
        for (int i = 0, j = 0; j < this.options.keyBindings.length; i++) {
            if (j == this.options.keyBindings.length - 9 && i % verticalButtons + 9 >= verticalButtons)
                continue;  // Force the slots to be aligned
            this.buttonList.add(new GuiSmallButton(j, xCenter + (i / verticalButtons - 1) * rowSpan - buttonWidth, yMargin + (buttonHeight + buttonYMargin) * (i % verticalButtons), buttonWidth, buttonHeight, this.options.getOptionDisplayString(j)));
            j++;
        }
        this.buttonList.add(new GuiButton(200, xCenter - 100, (3 * yMargin / 2) + (buttonHeight + buttonYMargin) * verticalButtons, I18n.getString("gui.done")));
        this.screenTitle = I18n.getString("controls.title");
    }

    /**
     * @author Juloos
     * @reason Bad height positioning logic
     */
    @Overwrite
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
        for (int i = 0; i < this.options.keyBindings.length; ++i) {
            boolean hasConflicts = false;
            for (int j = 0; j < this.options.keyBindings.length; ++j) {
                if (j == i || this.options.keyBindings[i].keyCode != this.options.keyBindings[j].keyCode)
                    continue;
                hasConflicts = true;
                break;
            }
            GuiButton button = ((GuiButton) this.buttonList.get(i));
            button.displayString = this.buttonId == i ? EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + "??? " + EnumChatFormatting.WHITE + "<" : (hasConflicts ? EnumChatFormatting.RED + this.options.getOptionDisplayString(i) : this.options.getOptionDisplayString(i));
            this.drawString(this.fontRenderer, this.options.getKeyBindingDescription(i), button.xPosition + button.width + buttonXMargin, button.yPosition + (button.height - this.fontRenderer.FONT_HEIGHT + 1) / 2, -1);
        }
        super.drawScreen(par1, par2, par3);
    }
}
