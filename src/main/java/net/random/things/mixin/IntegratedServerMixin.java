package net.random.things.mixin;

import btw.random.things.RandomThingsAddon;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Inject(
            method = "shareToLAN",
            at = @At(value = "RETURN", ordinal = 0)
    )
    private void shareToLAN(EnumGameType par1EnumGameType, boolean par2, CallbackInfoReturnable<String> cir) {
        if (!RandomThingsAddon.autoHealthScoreboard)
            return;
        Scoreboard scoreboard = ((IntegratedServer) (Object) this).getEntityWorld().getScoreboard();
        ScoreObjective health;
        try {
            health = scoreboard.func_96535_a("health", ScoreObjectiveCriteria.health);
        } catch (IllegalArgumentException e) {
            return;
        }
        health.setDisplayName("§c❤§r");
        scoreboard.func_96530_a(0, health);  // Display on list
        scoreboard.func_96530_a(2, health);  // Display on belowName
    }
}
