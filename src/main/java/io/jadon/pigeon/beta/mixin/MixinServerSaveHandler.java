package io.jadon.pigeon.beta.mixin;

import net.minecraft.server.entity.AbstractPlayerManager;
import net.minecraft.server.world.ServerSaveHandler;
import net.minecraft.world.AbstractSaveHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerSaveHandler.class)
public abstract class MixinServerSaveHandler implements AbstractSaveHandler, AbstractPlayerManager {

    // Expected to be there
    public AbstractPlayerManager getPlayerFileManager() {
        return this;
    }

}
