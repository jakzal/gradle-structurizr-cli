package pl.zalas.gradle.structurizrcli.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class Export : DefaultTask() {

    @Input
    val version: Property<String> = project.objects.property(String::class.java)

    @Input
    val workspace: Property<String> = project.objects.property(String::class.java)

    @Input
    val format: Property<String> = project.objects.property(String::class.java)

    init {
        group = "documentation"
        description = "Runs the export Structurizr CLI command"
    }

    @TaskAction
    fun export() {
        project.javaexec { spec ->
            spec.workingDir(project.projectDir)
            spec.classpath(project.files(structurizrCliJar(version.get())))
            spec.args("export", "-workspace", workspace.get(), "-format", format.get())
        }
    }

    private fun structurizrCliDir() = "${project.buildDir}/structurizr-cli"

    private fun structurizrCliJar(version: String) =
            "${structurizrCliDir()}/structurizr-cli-$version.jar"
}