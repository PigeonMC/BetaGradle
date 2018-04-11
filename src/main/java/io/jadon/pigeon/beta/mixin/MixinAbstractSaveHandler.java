package io.jadon.pigeon.beta.mixin;

import net.minecraft.server.entity.AbstractPlayerManager;
import net.minecraft.world.AbstractSaveHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractSaveHandler.class)
public interface MixinAbstractSaveHandler {

    AbstractPlayerManager getPlayerFileManager();

}
