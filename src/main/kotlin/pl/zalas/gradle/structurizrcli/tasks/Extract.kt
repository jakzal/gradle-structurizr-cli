package pl.zalas.gradle.structurizrcli.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class Extract : DefaultTask() {
    @Input
    val version: Property<String> = project.objects.property(String::class.java)

    init {
        group = "documentation"
        description = "Extracts the Structurizr CLI zip"
    }

    @TaskAction
    fun extract() {
        project.copy { spec ->
            spec.from(project.zipTree(downloadDest("${project.buildDir}/downloads", version.get())))
            spec.into(structurizrCliDir())
        }
    }

    private fun downloadDest(downloadsDir: String, version: String) =
            "$downloadsDir/structurizr-cli-$version.zip"

    private fun structurizrCliDir() = "${project.buildDir}/structurizr-cli"
}