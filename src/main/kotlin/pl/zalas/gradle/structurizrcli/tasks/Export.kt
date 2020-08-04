package pl.zalas.gradle.structurizrcli.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

open class Export : DefaultTask() {

    @InputFile
    val workspace: RegularFileProperty = project.objects.fileProperty()

    @Input
    val format: Property<String> = project.objects.property(String::class.java)

    @InputFile
    val structurizrCliJar: RegularFileProperty = project.objects.fileProperty()

    init {
        group = "documentation"
        description = "Runs the export Structurizr CLI command"
    }

    @TaskAction
    fun export() {
        project.javaexec { spec ->
            spec.workingDir(project.layout.projectDirectory)
            spec.classpath(structurizrCliJar.get())
            spec.args("export", "-workspace", workspace.get(), "-format", format.get())
        }
    }
}