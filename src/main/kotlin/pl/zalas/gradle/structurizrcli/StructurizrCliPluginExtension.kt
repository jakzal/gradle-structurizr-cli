package pl.zalas.gradle.structurizrcli

open class StructurizrCliPluginExtension {
    var version: String? = "1.3.1"
    var export: Map<String, List<String>> = emptyMap()
}
