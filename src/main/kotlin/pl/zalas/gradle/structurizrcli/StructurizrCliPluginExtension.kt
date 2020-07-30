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
