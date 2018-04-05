package io.jadon.pigeon.beta

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File

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
            when (parts[0]) {
                "overwrite" -> {
                    overwriteClasses.add(parts[1])
                }
                "inject" -> {
                    injectClasses.add(parts[1])
                }
            }
        }

        // TODO: Inject classes

        project.dependencies.add("compile", project.files(finishedFile))
    }

}
