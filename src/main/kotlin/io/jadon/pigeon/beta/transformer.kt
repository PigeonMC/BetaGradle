package io.jadon.pigeon.beta

import net.minecraft.launchwrapper.IClassTransformer
import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins
import java.io.File

open class ClassCacheTransformer : IClassTransformer {

    companion object {
        /**
         * This needs to be set by the user
         */
        var CLASSES_TO_CACHE: List<String> = listOf()
    }

    val cache = mutableMapOf<String, ByteArray>()

    override fun transform(name: String, transformedName: String, classData: ByteArray?): ByteArray? {
        // cache if the name is in the list of classes to cache
        classData?.let { if (CLASSES_TO_CACHE.contains(name)) cache.put(name, it) }
        return classData
    }

}

class InjectionTweaker : ITweaker {

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
        MixinBootstrap.init()
        MixinEnvironment.getDefaultEnvironment().side = MixinEnvironment.Side.UNKNOWN
        Mixins.addConfiguration("mixins.beta.json")

        classLoader.registerTransformer("io.jadon.pigeon.beta.ClassCacheTransformer")
    }

    override fun acceptOptions(args: MutableList<String>, gameDir: File?, assetsDir: File?, profile: String?) {
    }

    override fun getLaunchArguments(): Array<String> = arrayOf()

    override fun getLaunchTarget(): String = ""

}
