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

import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.JavaExec

class StructurizrCliPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("structurizrCli", StructurizrCliPluginExtension::class.java)

        registerDownloadTask(project, extension)
        registerExtractTask(project, extension)
        registerExportTask(project, extension)
    }

    private fun registerDownloadTask(project: Project, extension: StructurizrCliPluginExtension) {
        project.tasks.register("structurizrCliDownload", Download::class.java) { task ->
            task.group = "documentation"
            task.description = "Downloads the Structurizr CLI zip"
            task.src(downloadUrl(extension))
            task.dest(downloadsDir(project, extension))
            task.overwrite(false)
        }
    }

    private fun registerExtractTask(project: Project, extension: StructurizrCliPluginExtension) {
        project.tasks.register("structurizrCliExtract", Copy::class.java) { task ->
            task.group = "documentation"
            task.description = "Extracts the Structurizr CLI zip"
            task.dependsOn("structurizrCliDownload")
            task.from(project.zipTree(downloadsDir(project, extension)))
            task.into(structurizrCliDir(project))
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
            extension.export.flatMap { export ->
                val format = export.key
                export.value.mapIndexed { index, workspace ->
                    project.tasks.register("structurizrCliExport-$format$index", JavaExec::class.java) { task ->
                        task.dependsOn("structurizrCliExtract")
                        task.workingDir(project.projectDir)
                        task.classpath(project.files(structurizrCliJar(project, extension)))
                        task.args("export", "-workspace", workspace, "-format", format)
                    }
                }
            }
        }
    }

    private fun downloadUrl(extension: StructurizrCliPluginExtension) =
            "https://github.com/structurizr/cli/releases/download/v${extension.version}/structurizr-cli-${extension.version}.zip"

    private fun downloadsDir(project: Project, extension: StructurizrCliPluginExtension) =
            "${project.buildDir}/downloads/structurizr-cli-${extension.version}.zip"

    private fun structurizrCliDir(project: Project) = "${project.buildDir}/structurizr-cli"

    private fun structurizrCliJar(project: Project, extension: StructurizrCliPluginExtension) =
            "${structurizrCliDir(project)}/structurizr-cli-${extension.version}.jar"
}
