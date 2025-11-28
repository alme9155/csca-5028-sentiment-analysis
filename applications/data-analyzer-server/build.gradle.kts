plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "cu.csca5028.alme9155.analyzer"

val ktorVersion: String by project

application {
    version = rootProject.version as String
    applicationName = project.name
    mainClass.set("cu.csca5028.alme9155.analyzer.AppKt")
}

dependencies {
    implementation(project(":support:workflow-support"))
    implementation(project(":support:logging-support"))
    implementation(project(":components:sentiment"))
    implementation(project(":components:database"))
    implementation(project(":components:messaging"))

    //implementation("org.slf4j:slf4j-nop:2.0.16")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.3.2")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktorVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
    testImplementation("io.mockk:mockk:1.13.14")

}
application {
    mainClass.set("cu.csca5028.alme9155.analyzer.AppKt")

    applicationName = project.name
    version = rootProject.version as String
}
tasks.withType<JavaExec>().configureEach {
    if (name == "run") {
        mainClass.set("cu.csca5028.alme9155.analyzer.AppKt")
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "cu.csca5028.alme9155.analyzer.AppKt"
    }
    // Turn the normal JAR into a runnable fat JAR
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })
}