package io.github.gaming32.literalskyblock.forge;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;

public class HackShaderInstanceResourceLocation {
    public static ThreadLocal<String> namespace = new ThreadLocal<>();

    public static ShaderInstance create(ResourceProvider factory, ResourceLocation resourceLocation, VertexFormat format) throws IOException {
        namespace.set(resourceLocation.getNamespace());
        final ShaderInstance result = new ShaderInstance(factory, resourceLocation.getPath(), format);
        namespace.set(null);
        return result;
    }
}
