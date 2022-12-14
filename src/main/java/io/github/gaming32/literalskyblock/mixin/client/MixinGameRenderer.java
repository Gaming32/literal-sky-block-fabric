package io.github.gaming32.literalskyblock.mixin.client;

import com.mojang.datafixers.util.Pair;
import io.github.gaming32.literalskyblock.client.LSBClient;
import io.github.gaming32.literalskyblock.forge.RegisterShadersEvent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Unique
    private ResourceManager forge$resourceManager;

    @Inject(method = "reloadShaders", at = @At("HEAD"))
    private void storeResourceManager(ResourceManager manager, CallbackInfo ci) {
        forge$resourceManager = manager;
    }

    @Redirect(
        method = "reloadShaders",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
        )
    )
    private boolean implementShadersEvent(List<Pair<ShaderInstance, Consumer<ShaderInstance>>> instance, Object e) throws IOException {
        @SuppressWarnings("unchecked")
        final var shader = (Pair<ShaderInstance, Consumer<ShaderInstance>>)e;
        instance.add(shader);
        if (shader.getFirst().getName().equals("rendertype_crumbling")) {
            LSBClient.registerShaders(new RegisterShadersEvent(forge$resourceManager, instance));
        }
        return true;
    }
}
