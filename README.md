# Structurizr CLI gradle plugin

Enables Gradle to run [Structurizr CLI](https://github.com/structurizr/cli) commands.

## Configuration


### Gradle

```groovy
plugins {
    id('pl.zalas.gradle.structurizrcli') version '1.0.0'
}

structurizrCli {
    version = "1.3.1"
    export = [
        "plantuml": ["docs/diagrams/workspace.dsl"]
    ]
}
```

### Kotlin

```kotlin
plugins {
    id("pl.zalas.gradle.structurizrcli") version "1.0.0"
}

structurizrCli {
    version = "1.3.1"
    export = mapOf(
        "plantuml" to listOf("docs/diagrams/mastermind.dsl")
    )
}
```

## Tasks

* `structurizrCliDownload` - downloads the Structurizr CLI zip file
* `structurizrCliExtract` - extracts the downloaded Structurizr CLI zip file
* `structurizrCliExport` - exports diagrams with Structurizr CLI
