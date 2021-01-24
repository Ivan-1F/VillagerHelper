package me.ivan.villagerhelper.mixin;

import me.ivan.villagerhelper.config.Configs;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(at = @At("HEAD"), method = "init")
	private void init(CallbackInfo ci) {
		Configs.readConfigFile();
	}
}
