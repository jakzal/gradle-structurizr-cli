package pl.zalas.gradle.structurizrcli.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option
import org.gradle.process.ExecOperations
import javax.inject.Inject

open class Push @Inject constructor(@Internal val execOperations: ExecOperations) : DefaultTask() {

    @InputFile
    val structurizrCliJar: RegularFileProperty = project.objects.fileProperty()

    @Input
    val id: Property<String> = project.objects.property(String::class.java)

    @Input
    val key: Property<String> = project.objects.property(String::class.java)

    @Input
    val secret: Property<String> = project.objects.property(String::class.java)

    @InputFile
    @Optional
    val workspace: RegularFileProperty = project.objects.fileProperty()

    @InputDirectory
    @Optional
    val docs: DirectoryProperty = project.objects.directoryProperty()

    @InputDirectory
    @Optional
    val adrs: DirectoryProperty = project.objects.directoryProperty()

    @Input
    @Optional
    val url: Property<String> = project.objects.property(String::class.java)

    @Input
    @Optional
    val passphrase: Property<String> = project.objects.property(String::class.java)

    @OutputDirectory
    val structurizrCliDirectory: DirectoryProperty = project.objects.directoryProperty()

    @Option(option = "id", description = "Workspace ID")
    fun setId(id: String) {
        this.id.set(id)
    }

    @Option(option = "key", description = "API key")
    fun setKey(key: String) {
        this.key.set(key)
    }

    @Option(option = "secret", description = "API secret")
    fun setSecret(secret: String) {
        this.secret.set(secret)
    }

    @Option(option = "workspace", description = "The path to the file or directory containing a definition of the workspace")
    fun setWorkspace(workspace: String) {
        this.workspace.set(project.file(workspace))
    }

    @Option(option = "docs", description = "The path to the directory containing Markdown/AsciiDoc files to be published")
    fun setDocs(docs: String) {
        this.docs.set(project.file(docs))
    }

    @Option(option = "adrs", description = "The path to the directory containing ADRs")
    fun setAdrs(adrs: String) {
        this.adrs.set(project.file(adrs))
    }

    @Option(option = "url", description = "The Structurizr API URL (optional)")
    fun setUrl(url: String) {
        this.url.set(url)
    }

    @Option(option = "passphrase", description = "he passphrase to use (optional; only required if client-side encryption enabled on the workspace)")
    fun setPassphrase(passphrase: String) {
        this.passphrase.set(passphrase)
    }

    @TaskAction
    fun push() {
        execOperations.javaexec { spec ->
            spec.workingDir(project.layout.projectDirectory)
            spec.classpath(structurizrCliJar.get(), structurizrCliDirectory.dir("lib/*"))
            spec.mainClass.set("com.structurizr.cli.StructurizrCliApplication")
            spec.args(args("push"))
        }
    }

    private fun args(name: String) = listOf(name) +
            id.arg("-id") +
            key.arg("-key") +
            secret.arg("-secret") +
            url.arg("-url") +
            workspace.arg("-workspace") +
            docs.arg("-docs") +
            adrs.arg("-adrs") +
            passphrase.arg("-passphrase")

    private fun <T : Any> Property<T>.arg(name: String): List<String> =
            if (isPresent) listOf(name, get().toString()) else emptyList()
}
