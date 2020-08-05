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

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class StructurizrCliPluginTest {
    @Test
    fun `it registers the structurizrCliVersion task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("pl.zalas.structurizr-cli")

        assertNotNull(project.tasks.findByName("structurizrCliVersion"))
    }

    @Test
    fun `it registers the structurizrCliDownload task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("pl.zalas.structurizr-cli")

        assertNotNull(project.tasks.findByName("structurizrCliDownload"))
    }

    @Test
    fun `it registers the structurizrCliExtract task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("pl.zalas.structurizr-cli")

        assertNotNull(project.tasks.findByName("structurizrCliExtract"))
    }

    @Test
    fun `it registers the structurizrCliExport task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("pl.zalas.structurizr-cli")

        assertNotNull(project.tasks.findByName("structurizrCliExport"))
    }

    @Test
    fun `it registers the structurizrCliPull task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("pl.zalas.structurizr-cli")

        assertNotNull(project.tasks.findByName("structurizrCliPull"))
    }

    @Test
    fun `it registers the structurizrCliPush task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("pl.zalas.structurizr-cli")

        assertNotNull(project.tasks.findByName("structurizrCliPush"))
    }
}
