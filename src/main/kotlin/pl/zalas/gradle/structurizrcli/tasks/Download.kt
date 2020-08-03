package pl.zalas.gradle.structurizrcli.tasks

import de.undercouch.gradle.tasks.download.DownloadAction
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class Download : DefaultTask() {
    private val action = DownloadAction(project, this)

    @Input
    val version: Property<String> = project.objects.property(String::class.java)

    init {
        group = "documentation"
        description = "Downloads the Structurizr CLI zip"
    }

    @TaskAction
    fun download() {
        action.apply {
            src(downloadUrl(version.get()))
            dest(downloadDest("${project.buildDir}/downloads", version.get()))
            onlyIfModified(true)
            execute()
        }
    }

    private fun downloadUrl(version: String): String {
        return "https://github.com/structurizr/cli/releases/download/v$version/structurizr-cli-$version.zip"
    }

    private fun downloadDest(downloadsDir: String, version: String) =
            "$downloadsDir/structurizr-cli-$version.zip"
}