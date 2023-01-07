package io.github.gaming32.literalskyblock.mixin.client;

import io.github.gaming32.literalskyblock.forge.HackShaderInstanceResourceLocation;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.FileNotFoundException;

@Mixin(ShaderInstance.class)
public class MixinShaderInstance {
    @ModifyVariable(method = "<init>", at = @At("STORE"), ordinal = 0)
    private ResourceLocation changeResourceLocationInit(ResourceLocation value) {
        final String namespace = HackShaderInstanceResourceLocation.namespace.get();
        if (namespace != null) {
            value = new ResourceLocation(namespace, value.getPath());
        }
        return value;
    }

    @Redirect(
        method = "getOrCreate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/resources/ResourceProvider;getResourceOrThrow(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/server/packs/resources/Resource;"
        )
    )
    private static Resource changeResourceLocationGetOrCreate(ResourceProvider instance, ResourceLocation resourceLocation) throws FileNotFoundException {
        final String namespace = HackShaderInstanceResourceLocation.namespace.get();
        if (namespace != null) {
            resourceLocation = new ResourceLocation(namespace, resourceLocation.getPath());
        }
        return instance.getResourceOrThrow(resourceLocation);
    }
}
