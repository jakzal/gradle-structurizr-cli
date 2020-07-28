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

    private fun givenConfiguration(projectDir: File, gradleBuildFile: String) {
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText(gradleBuildFile)
    }

    private fun execute(projectDir: File, task: String) = GradleRunner.create().run {
        forwardOutput()
        withPluginClasspath()
        withArguments(task)
        withProjectDir(projectDir)
        build()
    }
}
