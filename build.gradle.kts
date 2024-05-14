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


tasks.withType<Test> {
    val harnessJavaAgent = System.getProperty("HARNESS_JAVA_AGENT")
    if (harnessJavaAgent != null) {
        jvmArgs(harnessJavaAgent)
    }
}

gradle.projectsEvaluated {
    tasks.withType<Test> {
        filter {
            isFailOnNoMatchingTests = false
        }
    }
}
