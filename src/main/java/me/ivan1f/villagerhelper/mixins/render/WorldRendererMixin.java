package me.ivan1f.villagerhelper.mixins.render;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11500
import com.mojang.blaze3d.systems.RenderSystem;
import me.ivan1f.villagerhelper.renderers.PoiRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    //#if MC >= 11500
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE_STRING",
                    args = "ldc=weather",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"
            )
    )
    private void render$villagerhelper(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();

        PoiRenderer.getInstance().onWorldRenderLast(tickDelta);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }
    //#endif
}
