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
package pl.zalas.gradle.structurizrcli

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import pl.zalas.gradle.structurizrcli.tasks.Download
import pl.zalas.gradle.structurizrcli.tasks.Export
import pl.zalas.gradle.structurizrcli.tasks.Extract
import pl.zalas.gradle.structurizrcli.tasks.Version

class StructurizrCliPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        val extension = extensions.create("structurizrCli", StructurizrCliPluginExtension::class.java)
        val version = registerVersionTask(extension)
        val download = registerDownloadTask(version)
        val extract = registerExtractTask(version, download)
        registerExportTasks(extension, extract)
    }

    private fun Project.registerVersionTask(extension: StructurizrCliPluginExtension) =
            tasks.register("structurizrCliVersion", Version::class.java) { task ->
                task.version.set(extension.version)
            }

    private fun Project.registerDownloadTask(version: TaskProvider<Version>) =
            tasks.register("structurizrCliDownload", Download::class.java) { task ->
                task.version.set(version.flatMap { it.version })
                task.downloadDirectory.set(layout.buildDirectory.dir("downloads"))
                task.downloadUrlTemplate.set("https://github.com/structurizr/cli/releases/download/v{VERSION}/structurizr-cli-{VERSION}.zip")
            }

    private fun Project.registerExtractTask(version: TaskProvider<Version>, download: TaskProvider<Download>) =
            tasks.register("structurizrCliExtract", Extract::class.java) { task ->
                task.dependsOn("structurizrCliDownload")
                task.version.set(version.flatMap { it.version })
                task.structurizrCliDirectory.set(layout.buildDirectory.dir("structurizr-cli"))
                task.downloadDestination.set(download.flatMap { it.downloadDestination })
            }

    private fun Project.registerExportTasks(extension: StructurizrCliPluginExtension, extract: TaskProvider<Extract>) {
        tasks.register("structurizrCliExport") { task ->
            task.group = "documentation"
            task.description = "Runs the export Structurizr CLI command"
            task.dependsOn(tasks.matching {
                it.name.startsWith("structurizrCliExport-")
            })
        }
        afterEvaluate {
            // export tasks need to be created once configuration has been processed
            extension.exports.forEachIndexed { index, export ->
                tasks.register("structurizrCliExport-${export.format}$index", Export::class.java) { task ->
                    task.dependsOn("structurizrCliExtract")
                    task.workspace.set(layout.projectDirectory.file(export.workspace))
                    task.format.set(export.format)
                    task.structurizrCliJar.set(extract.flatMap { it.structurizrCliJar })
                }
            }
        }
    }
}
