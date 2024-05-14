plugins {
    kotlin("multiplatform") version "1.9.21" apply false
    id("org.jetbrains.kotlinx.kover") version "0.7.6" apply false
}

val isReleaseBuild: Boolean
    get() = properties.containsKey("release")

allprojects {
    repositories {
        mavenCentral()
    }
    val artifactPublishVersion: String by project
    val artifactGroupId: String by project
    group = artifactGroupId
    version = if (isReleaseBuild) artifactPublishVersion else "main-SNAPSHOT"

    tasks.withType<Test> {
        // Print to indicate entering the Test task configuration
        println("Configuring Test tasks...")

        val harnessJavaAgent = System.getProperty("HARNESS_JAVA_AGENT")
        if (harnessJavaAgent != null) {
            println("HARNESS_JAVA_AGENT found: $harnessJavaAgent")
            jvmArgs(harnessJavaAgent)
        } else {
            println("HARNESS_JAVA_AGENT not found.")
        }
    }

    gradle.projectsEvaluated {
        tasks.withType<Test> {
            println("Configuring test filters after projects are evaluated...")

            filter {
                // Print the configuration setting for failing on no matching tests
                println("Setting isFailOnNoMatchingTests to false.")
                isFailOnNoMatchingTests = false
            }
        }
    }
}

