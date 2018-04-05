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

const val UNFINISHED_JAR_PATH = ""
const val FINISHED_JAR_PATH = ""

open class BetaMinecraftPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.create("betaMinecraft", BetaMinecraftTask::class.java)
    }
}

open class BetaMinecraftTask : DefaultTask() {

    @TaskAction
    fun betaMinecraft() {
        val unfinishedFile = File(UNFINISHED_JAR_PATH)
        val finishedFile = File(FINISHED_JAR_PATH)

        // TODO: remove when ready to publish
        if (false && finishedFile.exists()) {
            println("Finished jar already exists")
            return
        }

        val lines = this.javaClass.getResource("classOverrides.txt").readText().split("\n").mapNotNull {
            val s = it.trim()
            if (s.isEmpty() || s.startsWith("#")) null else s
        }

        val overwriteClasses = mutableListOf<String>()
        val injectClasses = mutableListOf<String>()

        lines.forEach {
            val parts = it.split(" ")
            val clazz = parts[1].replace('.', '/').let { if (it.endsWith(".class")) it else "$it.class" }
            when (parts[0]) {
                "overwrite" -> {
                    overwriteClasses.add(clazz)
                }
                "inject" -> {
                    injectClasses.add(clazz)
                }
            }
        }

        val unfinishedJar = ZipFile(unfinishedFile)
        val classes = unfinishedJar.entries().toList().filter { !overwriteClasses.contains(it.name) }.map {
            val stream = unfinishedJar.getInputStream(it)
            val bytes = stream.readBytes()
            stream.close()
            it.name to bytes
        }.toMutableList()

        classes.addAll(overwriteClasses.map {
            val stream = this.javaClass.getResourceAsStream(it)
            val bytes = stream.readBytes()
            stream.close()
            it to bytes
        })

        // TODO: This won't be needed when the top is removed
        if (finishedFile.exists()) finishedFile.delete()

        val finishedJarStream = ZipOutputStream(FileOutputStream(finishedFile))
        val output = BufferedOutputStream(finishedJarStream)

        classes.forEach {
            finishedJarStream.putNextEntry(ZipEntry(it.first))
            output.write(it.second)
            output.flush()
            finishedJarStream.closeEntry()
        }
        output.close()
        finishedJarStream.close()

        project.dependencies.add("compile", project.files(finishedFile))
    }

}
