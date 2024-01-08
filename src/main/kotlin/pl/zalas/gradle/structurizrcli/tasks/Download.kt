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

    // Example: https://github.com/structurizr/cli/releases/download/2024.01.02/structurizr-cli.zip
    @Input
    val downloadUrlTemplate: Property<String> = project.objects.property(String::class.java)

    // Example: https://github.com/structurizr/cli/releases/download/v1.35.0/structurizr-cli-1.35.0.zip
    @Input
    val legacyDownloadUrlTemplate: Property<String> = project.objects.property(String::class.java)

    @Input
    val downloadUrl: Provider<String> = version.flatMap { v ->
        when {
            v.matches("^[0-9]{4}\\..*".toRegex()) -> downloadUrlTemplate.map { t -> t.replace("{VERSION}", v) }
            else -> legacyDownloadUrlTemplate.map { t -> t.replace("{VERSION}", v) }
        }
    }

    @OutputDirectory
    val downloadDirectory: DirectoryProperty = project.objects.directoryProperty()

    @OutputFile
    val downloadDestination: Provider<RegularFile> =
        version.flatMap { v -> downloadDirectory.file("structurizr-cli-$v.zip") }

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