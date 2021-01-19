package me.ivan.villagerhelper.mixin;

import me.ivan.villagerhelper.VillagerHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SettingsScreen.class)
public class SettingsScreenMixin extends Screen {
    private SettingsScreenMixin() {
        super(null);
    }

    private ButtonWidget villagerHelperToggleButton;

    @Inject(at = @At("RETURN"), method = "init")
    private void drawMenuButton(CallbackInfo ci) {
        this.villagerHelperToggleButton = this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 + 144 - 6, 150, 20, new TranslatableText(VillagerHelper.enable ? "villagerhelper.gui.enabled" : "villagerhelper.gui.disabled").getString(), (button -> {
            VillagerHelper.enable = !VillagerHelper.enable;
            villagerHelperToggleButton.setMessage(new TranslatableText(VillagerHelper.enable ? "villagerhelper.gui.enabled" : "villagerhelper.gui.disabled").getString());
        })));
    }

}
