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

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class StructurizrCliPluginFunctionalTest {

    @Test
    fun `it downloads structurizr cli`(@TempDir projectDir: File) {
        givenConfiguration(projectDir, """
            plugins {
                id('pl.zalas.gradle.structurizrcli')
            }
        """)

        execute(projectDir, "structurizrCliDownload")

        assertTrue(File("${projectDir.absolutePath}/build/downloads/structurizr-cli-1.3.1.zip").exists())
    }

    @Test
    fun `it downloads structurizr cli in the configured version`(@TempDir projectDir: File) {
        givenConfiguration(projectDir, """
            plugins {
                id('pl.zalas.gradle.structurizrcli')
            }
            structurizrCli {
                version = "1.3.0"
            }
        """)

        execute(projectDir, "structurizrCliDownload")

        assertTrue(File("${projectDir.absolutePath}/build/downloads/structurizr-cli-1.3.0.zip").exists())
    }

    @Test
    fun `it extracts the downloaded structurizr cli`(@TempDir projectDir: File) {
        givenConfiguration(projectDir, """
            plugins {
                id('pl.zalas.gradle.structurizrcli')
            }
            structurizrCli {
                version = "1.3.1"
            }
        """)

        execute(projectDir, "structurizrCliExtract")

        assertTrue(File("${projectDir.absolutePath}/build/structurizr-cli/structurizr-cli-1.3.1.jar").exists())
    }

    @Test
    fun `it exports the workspace in the configured format`(@TempDir projectDir: File) {
        givenWorkspace(projectDir, "workspace.dsl")
        givenConfiguration(projectDir, """
            plugins {
                id('pl.zalas.gradle.structurizrcli')
            }
            structurizrCli {
                version = "1.3.1"
                export {
                    format = "plantuml"
                    workspace = "${projectDir.absolutePath}/workspace.dsl"
                }
            }
        """)

        execute(projectDir, "structurizrCliExport")

        assertTrue(File("${projectDir.absolutePath}/structurizr-SystemContext.puml").exists())
    }

    private fun givenConfiguration(projectDir: File, gradleBuildFile: String) {
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText(gradleBuildFile.trimIndent())
    }

    private fun givenWorkspace(projectDir: File, workspaceFileName: String) {
        val content = """
            workspace "Getting Started" "This is a model of my software system." {

            model {
                user = person "User" "A user of my software system."
                softwareSystem = softwareSystem "Software System" "My software system."

                user -> softwareSystem "Uses"
            }

            views {
                systemContext softwareSystem "SystemContext" "An example of a System Context diagram." {
                    include *
                    autoLayout
                }
            }
        }
        """
        projectDir.resolve(workspaceFileName).writeText(content.trimIndent())
    }

    private fun execute(projectDir: File, task: String) = GradleRunner.create().run {
        forwardOutput()
        withPluginClasspath()
        withArguments(task)
        withProjectDir(projectDir)
        build()
    }
}
