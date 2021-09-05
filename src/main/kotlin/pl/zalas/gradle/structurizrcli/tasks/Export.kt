/*
 * Copyright 2020 Jakub Zalas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.zalas.gradle.structurizrcli.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

open class Export : DefaultTask() {

    @InputFile
    val workspace: RegularFileProperty = project.objects.fileProperty()

    @Input
    val format: Property<String> = project.objects.property(String::class.java)

    @InputFile
    val structurizrCliJar: RegularFileProperty = project.objects.fileProperty()

    @OutputDirectory
    val structurizrCliDirectory: DirectoryProperty = project.objects.directoryProperty()

    init {
        group = "documentation"
        description = "Runs the export Structurizr CLI command"
    }

    @TaskAction
    fun export() {
        project.javaexec { spec ->
            spec.workingDir(project.layout.projectDirectory)
            spec.classpath(structurizrCliJar.get(), structurizrCliDirectory.dir("lib/*"))
            spec.mainClass.set("com.structurizr.cli.StructurizrCliApplication")
            spec.args("export", "-workspace", workspace.get(), "-format", format.get())
        }
    }
}