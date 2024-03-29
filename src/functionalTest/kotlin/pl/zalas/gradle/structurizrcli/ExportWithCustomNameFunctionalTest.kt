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

class ExportWithCustomNameFunctionalTest : FunctionalTest {

    @Test
    fun `it exports the workspace with custom name`(@TempDir projectDir: File) {
        givenWorkspace(projectDir, "workspace.dsl")
        givenConfiguration(projectDir, """
            plugins {
                id 'pl.zalas.structurizr-cli'
            }
            structurizrCli {
                export {
                    format = "plantuml"
                    workspace = "${projectDir.absolutePath}/workspace.dsl"
                    name = "myCustomName"
                }
            }
        """)

        execute(projectDir, "structurizrCliExport-myCustomName")

        assertTrue(File("${projectDir.absolutePath}/structurizr-SystemContext.puml").exists())
    }
}
