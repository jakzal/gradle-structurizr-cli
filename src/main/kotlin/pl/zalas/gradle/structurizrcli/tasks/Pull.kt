package pl.zalas.gradle.structurizrcli.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option
import org.gradle.process.ExecOperations
import javax.inject.Inject

open class Pull @Inject constructor(@Internal val execOperations: ExecOperations) : DefaultTask() {

    @InputFile
    val structurizrCliJar: RegularFileProperty = project.objects.fileProperty()

    @Input
    val id: Property<String> = project.objects.property(String::class.java)

    @Input
    val key: Property<String> = project.objects.property(String::class.java)

    @Input
    val secret: Property<String> = project.objects.property(String::class.java)

    @Input
    @Optional
    val url: Property<String> = project.objects.property(String::class.java)

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

    @Option(option = "url", description = "The Structurizr API URL (optional)")
    fun setUrl(url: String) {
        this.url.set(url)
    }

    @TaskAction
    fun pull() {
        execOperations.javaexec { spec ->
            spec.workingDir(project.layout.projectDirectory)
            spec.classpath(structurizrCliJar.get(), structurizrCliDirectory.dir("lib/*"))
            spec.mainClass.set("com.structurizr.cli.StructurizrCliApplication")
            spec.args(args())
        }
    }

    private fun args() = listOf("pull", "-id", id.get(), "-key", key.get(), "-secret", secret.get()) +
            if (url.isPresent) listOf("-url", url.get()) else emptyList()
}