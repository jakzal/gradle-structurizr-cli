import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm")
    idea
    `maven-publish`
    id("com.gradle.plugin-publish") version "2.0.0"
}

group = "pl.zalas"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

idea {
    project {
        jdkName = JvmTarget.JVM_17.target
        languageLevel = IdeaLanguageLevel(JvmTarget.JVM_17.target)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("de.undercouch:gradle-download-task:5.6.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.0")
    testImplementation("org.wiremock:wiremock:3.13.1")
}

gradlePlugin {
    website.set("https://github.com/jakzal/gradle-structurizr-cli")
    vcsUrl.set("https://github.com/jakzal/gradle-structurizr-cli.git")
    plugins {
        create("structurizrCli") {
            id = "pl.zalas.structurizr-cli"
            displayName = "Gradle Structurizr CLI plugin"
            implementationClass = "pl.zalas.gradle.structurizrcli.StructurizrCliPlugin"
            description = "Enables Gradle to run Structurizr CLI commands."
            tags.set(listOf("structurizr", "structurizr-cli", "task", "diagrams", "diagrams-as-code", "plantuml", "mermaid", "websequencediagrams", "json"))
        }
    }
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
