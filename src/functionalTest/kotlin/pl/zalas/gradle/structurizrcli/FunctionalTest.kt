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
import java.io.File

interface FunctionalTest {

    fun givenConfiguration(projectDir: File, gradleBuildFile: String) {
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText(gradleBuildFile.trimIndent())
    }

    fun givenWorkspace(projectDir: File, workspaceFileName: String) {
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

    fun execute(projectDir: File, task: String, arguments: List<String> = emptyList()) = GradleRunner.create().run {
        forwardOutput()
        withPluginClasspath()
        withArguments(listOf(task) + arguments)
        withProjectDir(projectDir)
        build()
    }
}
