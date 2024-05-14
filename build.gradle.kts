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
}


// This block will configure the Test tasks to use a specific JVM argument if it is provided via a system property.
tasks.withType<Test> {
    println "Configuring Test tasks..."

    val harnessJavaAgent = System.getProperty("HARNESS_JAVA_AGENT")
    if (harnessJavaAgent != null) {
        println "Setting JVM args for Test tasks: $harnessJavaAgent"
        jvmArgs(harnessJavaAgent)
    } else {
        println "No HARNESS_JAVA_AGENT provided"
    }
}

// This block is executed once all projects are evaluated, and it configures the behavior of Test tasks further.
gradle.projectsEvaluated {
    println "All projects have been evaluated. Configuring Test tasks post-evaluation..."
    
    tasks.withType<Test> {
        println "Setting Test task filters..."
        filter {
            isFailOnNoMatchingTests = false
        }
    }
}
