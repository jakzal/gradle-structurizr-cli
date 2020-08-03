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
import pl.zalas.gradle.structurizrcli.tasks.Download
import pl.zalas.gradle.structurizrcli.tasks.Export
import pl.zalas.gradle.structurizrcli.tasks.Extract

class StructurizrCliPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("structurizrCli", StructurizrCliPluginExtension::class.java)

        registerDownloadTask(project, extension)
        registerExtractTask(project, extension)
        registerExportTask(project, extension)
    }

    private fun registerDownloadTask(project: Project, extension: StructurizrCliPluginExtension) {
        project.tasks.register("structurizrCliDownload", Download::class.java) { task ->
            task.version.set(extension.version)
        }
    }

    private fun registerExtractTask(project: Project, extension: StructurizrCliPluginExtension) {
        project.tasks.register("structurizrCliExtract", Extract::class.java) { task ->
            task.dependsOn("structurizrCliDownload")
            task.version.set(extension.version)
        }
    }

    private fun registerExportTask(project: Project, extension: StructurizrCliPluginExtension) {
        project.tasks.register("structurizrCliExport") { task ->
            task.group = "documentation"
            task.description = "Runs the export Structurizr CLI command"
            task.dependsOn(project.tasks.matching {
                it.name.startsWith("structurizrCliExport-")
            })
        }
        project.afterEvaluate {
            // export tasks need to be created once configuration has been processed
            extension.exports.forEachIndexed { index, export ->
                project.tasks.register("structurizrCliExport-${export.format}$index", Export::class.java) { task ->
                    task.dependsOn("structurizrCliExtract")
                    task.version.set(extension.version)
                    task.workspace.set(export.workspace)
                    task.format.set(export.format)
                }
            }
        }
    }
}
