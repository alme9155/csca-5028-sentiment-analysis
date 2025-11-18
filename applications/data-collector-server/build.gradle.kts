plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")    
}

group = "cu.csca5028.alme9155.collector"

application {
    version = rootProject.version as String
    applicationName = project.name    
    mainClass.set("cu.csca5028.alme9155.collector.AppKt")
}

dependencies {
    implementation(project(":components:data-analyzer"))
    implementation(project(":support:workflow-support"))
    implementation(project(":support:logging-support"))
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.3.2")
    implementation("org.slf4j:slf4j-nop:2.0.16")
}
