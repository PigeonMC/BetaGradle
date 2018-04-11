package io.jadon.pigeon.beta.mixin;

import net.minecraft.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements Runnable, CommandListener, Minecraft {

    @Shadow
    public boolean running;

    @Override
    public boolean isRunning() {
        return this.running;
    }

}
