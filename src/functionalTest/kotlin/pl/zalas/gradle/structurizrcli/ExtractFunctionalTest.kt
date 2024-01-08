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

import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class ExtractFunctionalTest : FunctionalTest {

    @Test
    fun `it extracts the downloaded structurizr cli`(@TempDir projectDir: File) {
        givenConfiguration(projectDir, """
            plugins {
                id 'pl.zalas.structurizr-cli'
            }
            structurizrCli {
                version = "2024.01.02"
            }
        """)

        execute(projectDir, "structurizrCliExtract")

        assertTrue(File("${projectDir.absolutePath}/build/structurizr-cli/lib/structurizr-cli.jar").exists())
    }

    @Test
    fun `it extracts the downloaded legacy structurizr cli`(@TempDir projectDir: File) {
        givenConfiguration(projectDir, """
            plugins {
                id 'pl.zalas.structurizr-cli'
            }
            structurizrCli {
                version = "1.35.0"
            }
        """)

        execute(projectDir, "structurizrCliExtract")

        assertTrue(File("${projectDir.absolutePath}/build/structurizr-cli/lib/structurizr-cli-1.35.0.jar").exists())
    }

    @Test
    fun `it extracts the downloaded structurizr cli to a custom directory`(@TempDir projectDir: File) {
        givenConfiguration(projectDir, """
            plugins {
                id 'pl.zalas.structurizr-cli'
            }
            structurizrCli {
                version = "2024.01.02"
                extract {
                    directory = "structurizr-cli"
                }
            }
        """)

        execute(projectDir, "structurizrCliExtract")

        assertTrue(File("${projectDir.absolutePath}/structurizr-cli/lib/structurizr-cli.jar").exists())
    }
}
