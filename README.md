# Structurizr CLI gradle plugin

Enables Gradle to run [Structurizr CLI](https://github.com/structurizr/cli) commands.

## Configuration

```groovy
plugins {
    id('pl.zalas.gradle.structurizrcli')
}

structurizrCli {
    version = "1.3.1"
    export = ["plantuml": ["docs/diagrams/workspace.dsl"]]
}
```

## Tasks

* `structurizrCliDownload` - downloads the Structurizr CLI zip file
* `structurizrCliExtract` - extracts the downloaded Structurizr CLI zip file
* `structurizrCliExport` - exports diagrams with Structurizr CLI
