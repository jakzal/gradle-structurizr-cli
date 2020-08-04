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

import org.gradle.api.Action

open class StructurizrCliPluginExtension {
    var version: String? = "1.3.1"
    var exports: List<Export> = emptyList()
        private set

    fun export(action: Action<Export>) {
        Export().let { export ->
            action.execute(export)
            exports = exports + export
        }
    }

    data class Export(var format: String = "plantuml", var workspace: String = "workspace.dsl")
}
