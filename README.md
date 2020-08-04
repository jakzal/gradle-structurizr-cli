# Structurizr CLI Gradle plugin

Enables Gradle to run [Structurizr CLI](https://github.com/structurizr/cli) commands.

## Installation

### Groovy

```groovy
plugins {
    id 'pl.zalas.structurizr-cli' version '<version>'
}

structurizrCli {
}
```

### Kotlin

```kotlin
plugins {
    id("pl.zalas.structurizr-cli") version "<version>"
}

structurizrCli {
}
```

## Tasks

* `structurizrCliDownload` - downloads the Structurizr CLI zip file
* `structurizrCliExtract` - extracts the downloaded Structurizr CLI zip file
* `structurizrCliExport` - exports diagrams with Structurizr CLI

## Configuration

### Version

The latest Structurizr CLI version will be downloaded by default.
The `version` property can be used To force a specific release.

```kotlin
structurizrCli {
    version = "1.3.1"
}
```

### Export

The export task can be configured to generate a number of diagrams in formats of choice:

```kotlin
structurizrCli {
    export {
        format = "plantuml"
        workspace = "docs/diagrams/workspace.dsl"
    }
    export {
        format = "json"
        workspace = "docs/diagrams/workspace.dsl"
    }
}
```
