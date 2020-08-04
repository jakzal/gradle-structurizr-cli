package pl.zalas.gradle.structurizrcli.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class Version : DefaultTask() {

    @Input
    val version: Property<String> = project.objects.property(String::class.java)

    @TaskAction
    fun determine() {
        println("Structurizr CLI version ${version.get()}")
    }
}