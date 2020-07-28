import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    idea
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

idea {
    project {
        jdkName = "1.8"
        languageLevel = IdeaLanguageLevel("1.8")
    }
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("de.undercouch:gradle-download-task:4.1.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

gradlePlugin {
    val structurizrcli by plugins.creating {
        id = "pl.zalas.gradle.structurizrcli"
        implementationClass = "pl.zalas.gradle.structurizrcli.GradleStructurizrCliPlugin"
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
