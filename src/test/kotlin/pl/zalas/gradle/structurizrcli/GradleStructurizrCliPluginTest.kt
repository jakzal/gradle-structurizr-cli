/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package pl.zalas.gradle.structurizrcli

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * A simple unit test for the 'pl.zalas.gradle.structurizrcli.greeting' plugin.
 */
class GradleStructurizrCliPluginTest {
    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("pl.zalas.gradle.structurizrcli.greeting")

        // Verify the result
        assertNotNull(project.tasks.findByName("greeting"))
    }
}
