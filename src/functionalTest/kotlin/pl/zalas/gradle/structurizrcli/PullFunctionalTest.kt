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

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import pl.zalas.gradle.structurizrcli.test.StructurizrApiExtension
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

@ExtendWith(StructurizrApiExtension::class)
class PullFunctionalTest : FunctionalTest {

    @Test
    fun `it pulls the workspace from the Structurizr API`(@TempDir projectDir: File, structurizrApi: WireMockServer) {

        givenConfiguration(projectDir, """
            plugins {
                id 'pl.zalas.structurizr-cli'
            }
        """)

        structurizrApi.willRespondWithWorkspace("/workspace/48582")

        execute(projectDir, "structurizrCliPull", listOf(
                "--id", "48582",
                "--key", "e3defb35-ad64-48b5-9a53-4e22e0cf6f95",
                "--secret", "71b37e8a-b925-44cb-99e5-8e4b0dbd7620",
                "--url", "http://localhost:9090"
        ))

        assertTrue(File("${projectDir.absolutePath}/structurizr-48582-workspace.json").exists())
    }

    private fun WireMockServer.willRespondWithWorkspace(url: String) = stubFor(
            WireMock.get(WireMock.urlEqualTo(url))
                    .willReturn(
                            WireMock.aResponse()
                                    .withBody("""
                                        {
                                          "id" : 0,
                                          "name" : "Mastermind",
                                          "description" : "This is a model of the Mastermind code-breaking game.",
                                          "configuration" : { },
                                          "model" : {},
                                          "documentation": {},
                                          "views": {}
                                        }
                                        """.trimIndent())
                                    .withStatus(200)
                    )
    )
}
