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
* `structurizrCliPull` - pulls a workspace from the Structurizr API with Structurizr CLI
* `structurizrCliPush` - pushes content to a Structurizr workspace

## Configuration & usage

### Version

The latest Structurizr CLI version will be downloaded by default.
The `version` property can be used To force a specific release.

```kotlin
structurizrCli {
    version = "1.13.0"
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
    export {
        format = "plantuml"
        workspace = "docs/diagrams/workspace2.dsl"
        name = "SomeCustomName"
    }
}
```

```bash
./gradlew structurizrCliExport
```

### Pull

Pulls a workspace from the Structurizr API with Structurizr CLI.

```bash
./gradlew structurizrCliPull \
    --id 48582 \
    --key 6804d084-a11e-412b-8ce2-060a40557731 \
    --secret 453a265d-099e-491c-89b5-41945395bcfb
```

### Push

Pushes content to a Structurizr workspace.

```bash
./gradlew structurizrCliPush \
    --id 48582 \
    --key 6804d084-a11e-412b-8ce2-060a40557731 \
    --secret 453a265d-099e-491c-89b5-41945395bcfb
    --workspace workspace.dsl
```
