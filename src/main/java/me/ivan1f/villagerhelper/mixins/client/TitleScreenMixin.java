package me.ivan1f.villagerhelper.mixins.client;

import me.ivan1f.villagerhelper.config.Configs;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void readConfigFileWhenStart(CallbackInfo ci) {
        Configs.readConfigFile();
    }
}
