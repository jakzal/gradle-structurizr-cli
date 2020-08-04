package pl.zalas.gradle.structurizrcli.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*

open class Extract : DefaultTask() {

    @Input
    val version: Property<String> = project.objects.property(String::class.java)

    @InputFile
    val downloadDestination: RegularFileProperty = project.objects.fileProperty()

    @OutputDirectory
    val structurizrCliDirectory: DirectoryProperty = project.objects.directoryProperty()

    @OutputFile
    val structurizrCliJar: Provider<RegularFile> = version.flatMap { v -> structurizrCliDirectory.file("structurizr-cli-$v.jar") }

    init {
        group = "documentation"
        description = "Extracts the Structurizr CLI zip"
    }

    @TaskAction
    fun extract() {
        project.copy { spec ->
            spec.from(project.zipTree(downloadDestination))
            spec.into(structurizrCliDirectory)
        }
        println("Extracted ${structurizrCliJar.get()}")
    }
}