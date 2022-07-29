package me.ivan1f.villagerhelper.gui;

import me.ivan1f.villagerhelper.config.Configs;
import me.ivan1f.villagerhelper.network.ClientNetworkHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.client.options.Option;
import net.minecraft.text.TranslatableText;

//#if MC >= 11600
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif

public class ConfigScreen extends Screen {
    private final Screen parent;
    private ButtonListWidget listWidget;

    public ConfigScreen(Screen parent) {
        super(new TranslatableText("villagerhelper.gui.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        listWidget = new ButtonListWidget(
                //#if MC >= 11600
                //$$ this.client,
                //#else
                this.minecraft,
                //#endif
                this.width, this.height, 64, this.height - 32, 25
        );
        listWidget.addAll(new Option[]{
                new BooleanOption(
                        "villagerhelper.gui.config.toggle",
                        gameOptions -> Configs.ENABLE,
                        (gameOptions, aBoolean) -> Configs.ENABLE = aBoolean
                ),
                new DoubleOption(
                        "villagerhelper.gui.config.render_distance",
                        8, 512, 8,
                        gameOptions -> Configs.RENDER_DISTANCE,
                        (gameOptions, aDouble) -> Configs.RENDER_DISTANCE = aDouble,
                        (gameOptions, doubleOption) ->
                                //#if MC >= 11600
                                //$$ new TranslatableText("villagerhelper.gui.config.render_distance", Configs.RENDER_DISTANCE)
                                //#else
                                new TranslatableText("villagerhelper.gui.config.render_distance", Configs.RENDER_DISTANCE).asFormattedString()
                                //#endif
                )
        });
        this.children.add(listWidget);
        this.addButton(
                new ButtonWidget(
                        this.width / 2 - 100,
                        this.height / 6 + 168,
                        200,
                        20,
                        //#if MC >= 11600
                        //$$ new TranslatableText("villagerhelper.gui.done"),
                        //#else
                        new TranslatableText("villagerhelper.gui.done").asFormattedString(),
                        //#endif
                        (buttonWidget) -> this.onClose()
                )
        );
    }

    @Override
    public void onClose() {
        MinecraftClient.getInstance().openScreen(this.parent);
        Configs.writeConfigFile();
    }

    @Override
    public void render(
            //#if MC >= 11600
            //$$ MatrixStack matrices,
            //#endif
            int mouseX, int mouseY, float delta
    ) {
        this.renderBackground(
                //#if MC >= 11600
                //$$ matrices
                //#endif
        );
        this.listWidget.render(
                //#if MC >= 11600
                //$$ matrices,
                //#endif
                mouseX, mouseY, delta
        );
        //#if MC >= 11600
        //$$ this.textRenderer.draw(matrices, this.title, this.width / 2.0F, 15, 16777215);
        //#else
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 15, 16777215);
        //#endif

        //#if MC >= 11600
        //$$ this.textRenderer.draw(
        //$$                matrices,
        //$$                new TranslatableText(
        //$$                        ClientNetworkHandler.isServerModded() ? "villagerhelper.gui.server_modded" : "villagerhelper.gui.server_not_modded"
        //$$                ),
        //$$                this.width / 2.0F,
        //$$                28,
        //$$                16777215
        //$$        );
        //#else
        this.drawCenteredString(
                this.font,
                new TranslatableText(
                        ClientNetworkHandler.isServerModded() ? "villagerhelper.gui.server_modded" : "villagerhelper.gui.server_not_modded"
                ).asFormattedString(),
                this.width / 2,
                28,
                16777215
        );
        //#endif

        super.render(
                //#if MC >= 11600
                //$$ matrices,
                //#endif
                mouseX, mouseY, delta
        );
    }
}
