package me.ivan1f.villagerhelper.mixins.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import me.ivan1f.villagerhelper.renderers.PoiRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    //#if MC < 11500
    //$$ @Inject(
    //$$         method = "renderCenter",
    //$$         at = @At(
    //$$                 value = "FIELD",
    //$$                 target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z"
    //$$         )
    //$$ )
    //$$ private void render(float tickDelta, long endTime, CallbackInfo ci) {
    //$$     GlStateManager.pushMatrix();
    //$$     GlStateManager.enableBlend();
    //$$     GlStateManager.disableTexture();
    //$$
    //$$     PoiRenderer.getInstance().onWorldRenderLast(tickDelta);
    //$$
    //$$     GlStateManager.enableTexture();
    //$$     GlStateManager.disableBlend();
    //$$     GlStateManager.popMatrix();
    //$$ }
    //#endif
}
