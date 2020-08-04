package pl.zalas.gradle.structurizrcli.tasks

import de.undercouch.gradle.tasks.download.DownloadAction
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

open class Download : DefaultTask() {
    private val action = DownloadAction(project, this)

    @Input
    val version: Property<String> = project.objects.property(String::class.java)

    @Input
    val downloadUrlTemplate: Property<String> = project.objects.property(String::class.java)

    @Input
    val downloadUrl: Provider<String> = downloadUrlTemplate.flatMap { t -> version.map { v -> t.replace("{VERSION}", v) } }

    @OutputDirectory
    val downloadDirectory: DirectoryProperty = project.objects.directoryProperty()

    @OutputFile
    val downloadDestination: Provider<RegularFile> = version.flatMap { v -> downloadDirectory.file("structurizr-cli-$v.zip") }

    init {
        group = "documentation"
        description = "Downloads the Structurizr CLI zip"
    }

    @TaskAction
    fun download() {
        action.apply {
            src(downloadUrl.get())
            dest(downloadDestination.get().asFile)
            onlyIfModified(true)
            execute()
        }
    }
}