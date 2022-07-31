package me.ivan1f.villagerhelper.mixins.client;

import me.ivan1f.villagerhelper.gui.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11900
//$$ import net.minecraft.text.Text;
//#else
import net.minecraft.text.TranslatableText;
//#endif

@Mixin(SettingsScreen.class)
public class SettingsScreenMixin extends Screen {
    private SettingsScreenMixin() {
        super(null);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void createModConfigButton(CallbackInfo ci) {
        this.addButton(
                new ButtonWidget(
                        this.width / 2 + 5,
                        this.height / 6 + 144 - 6,
                        150,
                        20,
                        //#if MC >= 11900
                        //$$ Text.translatable("villagerhelper.gui.config_button"),
                        //#elseif MC >= 11600
                        //$$ new TranslatableText("villagerhelper.gui.config_button"),
                        //#else
                        new TranslatableText("villagerhelper.gui.config_button").asString(),
                        //#endif
                        button -> MinecraftClient.getInstance().openScreen(new ConfigScreen(this))
                )
        );
    }

}
