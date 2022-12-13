package io.github.gaming32.literalskyblock.forge;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.List;
import java.util.function.Consumer;

public class RegisterShadersEvent {
    private final ResourceManager resourceManager;
    private final List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderList;

    public RegisterShadersEvent(ResourceManager resourceManager, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderList) {
        this.resourceManager = resourceManager;
        this.shaderList = shaderList;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void registerShader(ShaderInstance shaderInstance, Consumer<ShaderInstance> onLoaded) {
        shaderList.add(Pair.of(shaderInstance, onLoaded));
    }
}
