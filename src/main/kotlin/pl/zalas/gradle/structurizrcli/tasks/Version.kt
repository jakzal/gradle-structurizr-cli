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
package pl.zalas.gradle.structurizrcli.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.net.URL

open class Version : DefaultTask() {

    @Input
    val version: Property<String> = project.objects.property(String::class.java).convention(latestVersionProvider())

    init {
        version.finalizeValueOnRead()
    }

    @TaskAction
    fun determine() {
        println("Structurizr CLI version ${version.get()}")
    }

    private fun latestVersionProvider(): Provider<String> = project.provider(this::latestVersion)

    private fun latestVersion(): String = URL("https://api.github.com/repos/structurizr/cli/releases/latest")
            .readText()
            .replace("(?smi).*?\"tag_name\":\\s*\"v([0-9.]*)\".*".toRegex(), "$1")
}
