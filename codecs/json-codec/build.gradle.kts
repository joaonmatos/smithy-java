

plugins {
    id("smithy-java.module-conventions")
    alias(libs.plugins.shadow)
}

description = "This module provides json functionality"

extra["displayName"] = "Smithy :: Java :: JSON"
extra["moduleName"] = "software.amazon.smithy.java.json"

dependencies {
    api(project(":core"))
    compileOnly(libs.jackson.core)
    testRuntimeOnly(libs.jackson.core)
}

tasks.shadowJar {
    configurations = listOf(project.configurations.compileClasspath.get())
    dependencies {
        include(
            dependency(
                libs.jackson.core
                    .get()
                    .toString(),
            ),
        )
        relocate("com.fasterxml.jackson.core", "software.amazon.smithy.java.internal.com.fasterxml.jackson.core")
    }
    archiveClassifier.set("")
    mergeServiceFiles()
}

artifacts {
    archives(tasks.shadowJar)
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()

        dependencies {
            include(
                dependency(
                    libs.jackson.core
                        .get()
                        .toString(),
                ),
            )
            relocate("com.fasterxml.jackson.core", "software.amazon.smithy.java.internal.shaded.com.fasterxml.jackson.core")
        }
    }
    jar {
        finalizedBy(shadowJar)
    }
}

(components["shadow"] as AdhocComponentWithVariants).addVariantsFromConfiguration(configurations.apiElements.get()) {
}

configurePublishing {
    customComponent = components["shadow"] as SoftwareComponent
}
