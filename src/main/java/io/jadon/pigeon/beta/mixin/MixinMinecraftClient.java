package io.jadon.pigeon.beta.mixin;

import net.minecraft.Minecraft;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements Runnable, Minecraft {

    @Shadow
    public boolean running;

    @Override
    public boolean isRunning() {
        return this.running;
    }

}
