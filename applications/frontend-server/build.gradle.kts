plugins {
    kotlin("jvm")
    application
}

group = "cu.csca5028.alme9155.web"

val ktorVersion: String by project

dependencies {
    implementation(project(":support:logging-support"))
    implementation(project(":support:workflow-support"))
    implementation(project(":components:sentiment"))
    implementation(project(":components:database"))

    //implementation("org.slf4j:slf4j-nop:2.0.16")

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-freemarker-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-html-builder-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")    

    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktorVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
    testImplementation("io.mockk:mockk:1.13.14")
}

application {
    mainClass.set("cu.csca5028.alme9155.web.AppKt")

    applicationName = project.name
    version = rootProject.version as String
}
tasks.withType<JavaExec>().configureEach {
    if (name == "run") {
        mainClass.set("cu.csca5028.alme9155.web.AppKt")
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "cu.csca5028.alme9155.web.AppKt"
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