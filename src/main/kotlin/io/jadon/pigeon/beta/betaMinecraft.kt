package io.jadon.pigeon.beta

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

const val UNFINISHED_JAR_PATH = "/unfinished.jar"
const val FINISHED_JAR_PATH = "build/minecraft/minecraft.jar"

open class BetaMinecraftPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.create("betaMinecraft", BetaMinecraftTask::class.java)
    }
}

open class BetaMinecraftTask : DefaultTask() {

    @TaskAction
    fun betaMinecraft() {
        val inputStream = this.javaClass.getResourceAsStream(UNFINISHED_JAR_PATH)
        val content = inputStream.readBytes()
        inputStream.close()
        val unfinishedFile = File.createTempFile("unfinished_minecraft", ".jar")
        unfinishedFile.writeBytes(content)

        val finishedFile = File(FINISHED_JAR_PATH)

        // TODO: remove when ready to publish
        if (false && finishedFile.exists()) {
            println("Finished jar already exists")
            return
        }

        val lines = this.javaClass.getResource("/classOverrides.txt").readText().split("\n").mapNotNull {
            val s = it.trim()
            if (s.isEmpty() || s.startsWith("#")) null else s
        }

        val overwriteClasses = mutableListOf<String>()
        val injectClasses = mutableListOf<String>()

        lines.forEach {
            val parts = it.split(" ")
            val clazz = "/" + parts[1].replace('.', '/').let { if (it.endsWith(".class")) it else "$it.class" }
            when (parts[0]) {
                "overwrite" -> {
                    overwriteClasses.add(clazz)
                }
                "inject" -> {
                    injectClasses.add(clazz)
                }
            }
        }

        ClassCacheTransformer.CLASSES_TO_CACHE = injectClasses

        val unfinishedJar = ZipFile(unfinishedFile)
        val classes = unfinishedJar.entries().toList().filter {
            !overwriteClasses.contains(it.name)
                    && !injectClasses.contains(it.name)
        }.map {
            val stream = unfinishedJar.getInputStream(it)
            val bytes = stream.readBytes()
            stream.close()
            it.name to bytes
        }.toMap().toMutableMap()

        classes.putAll(overwriteClasses.mapNotNull {
            val stream = this.javaClass.getResourceAsStream(it)
            if (stream == null) {
                println("$it FAILED")
                null
            } else {
                val bytes = stream.readBytes()
                stream.close()
                // remove slash in front
                it.substring(1) to bytes
            }
        }.toMap())

        // TODO: This won't be needed when the top is removed
        if (finishedFile.exists()) finishedFile.delete()

        val finishedJarStream = ZipOutputStream(FileOutputStream(finishedFile))
        val output = BufferedOutputStream(finishedJarStream)

        classes.forEach { (name, bytes) ->
            finishedJarStream.putNextEntry(ZipEntry(name))
            output.write(bytes)
            output.flush()
            finishedJarStream.closeEntry()
        }
        output.close()
        finishedJarStream.close()

        project.dependencies.add("compile", project.files(finishedFile))
    }

}
