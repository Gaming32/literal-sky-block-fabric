package io.github.gaming32.literalskyblock.mixin.client;

import io.github.gaming32.literalskyblock.forge.HackShaderInstanceResourceLocation;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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

    @ModifyVariable(method = "getOrCreate", at = @At("STORE"), ordinal = 0)
    private static ResourceLocation changeResourceLocationGetOrCreate(ResourceLocation value) {
        final String namespace = HackShaderInstanceResourceLocation.namespace.get();
        if (namespace != null) {
            value = new ResourceLocation(namespace, value.getPath());
        }
        return value;
    }
}
