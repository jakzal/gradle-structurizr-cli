import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm")
    idea
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.21.0"
}

group = "pl.zalas"

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

idea {
    project {
        jdkName = "1.8"
        languageLevel = IdeaLanguageLevel("1.8")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("de.undercouch:gradle-download-task:5.0.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.33.0")
}

gradlePlugin {
    plugins {
        create("structurizrCli") {
            id = "pl.zalas.structurizr-cli"
            displayName = "Gradle Structurizr CLI plugin"
            implementationClass = "pl.zalas.gradle.structurizrcli.StructurizrCliPlugin"
            description = "Enables Gradle to run Structurizr CLI commands."
        }
    }
}

pluginBundle {
    website = "https://github.com/jakzal/gradle-structurizr-cli"
    vcsUrl = "https://github.com/jakzal/gradle-structurizr-cli.git"
    tags = listOf("structurizr", "structurizr-cli", "task", "diagrams", "diagrams-as-code", "plantuml", "mermaid", "websequencediagrams", "json")
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations.getByName("functionalTestImplementation").extendsFrom(configurations.getByName("testImplementation"))
configurations.getByName("functionalTestRuntimeOnly").extendsFrom(configurations.getByName("testRuntimeOnly"))

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

val check by tasks.getting(Task::class) {
    dependsOn(functionalTest)
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events = mutableSetOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED, org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED, org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED)
        }
    }
    functionalTest {
        useJUnitPlatform()
        testLogging {
            events = mutableSetOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED, org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED, org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED)
        }
    }
}
