package me.ivan.villagerhelper.gui;

import me.ivan.villagerhelper.config.Configs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.client.options.Option;
import net.minecraft.text.TranslatableText;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private ButtonListWidget listWidget;

    public ConfigScreen(Screen parent) {
        super(new TranslatableText("VillagerHelper Config Screen"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        listWidget = new ButtonListWidget(this.minecraft, this.width, this.height, 64, this.height - 32, 25);
        listWidget.addAll(new Option[]{
                new BooleanOption(
                        "villagerhelper.gui.config.toggle",
                        gameOptions -> Configs.ENABLE,
                        (gameOptions, aBoolean) -> Configs.ENABLE = aBoolean
                ),
                new DoubleOption(
                        "villagerhelper.gui.config.render_distance",
                        0, 512, 8,
                        gameOptions -> Configs.RENDER_DISTANCE,
                        (gameOptions, aDouble) -> Configs.RENDER_DISTANCE = aDouble,
                        (gameOptions, doubleOption) -> new TranslatableText("villagerhelper.gui.config.render_distance", Configs.RENDER_DISTANCE).getString()
                )
        });
        this.children.add(listWidget);
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 168, 200, 20, new TranslatableText("villagerhelper.gui.config.done").getString(), (buttonWidget) -> {
            this.onClose();
        }));
    }

    @Override
    public void onClose() {
        MinecraftClient.getInstance().openScreen(this.parent);
        Configs.writeConfigFile();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.listWidget.render(mouseX, mouseY, delta);
        super.render(mouseX, mouseY, delta);
    }
}
