# Structurizr cli gradle plugin

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

* `structurizrCliDownload` - downloads the structurizr-cli zip file
* `structurizrCliExtract` - extracts the downloaded structurizr-cli zip file
* `structurizrCliExport` - exports diagrams with structurizr-cli
