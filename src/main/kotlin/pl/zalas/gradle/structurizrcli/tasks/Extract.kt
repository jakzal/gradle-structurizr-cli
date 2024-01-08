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
    val structurizrCliJar: Provider<RegularFile> = version.flatMap { v ->
        when {
            v.matches("^[0-9]{4}\\..*".toRegex()) -> structurizrCliDirectory.file("lib/structurizr-cli.jar")
            else -> structurizrCliDirectory.file("lib/structurizr-cli-$v.jar")
        }
    }

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