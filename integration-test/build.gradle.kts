plugins {
    kotlin("jvm") version "2.2.21"
}

val ktorVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":applications:frontend-server"))
    implementation(project(":applications:data-analyzer-server"))
    implementation(project(":applications:data-collector-server"))
    implementation(project(":components:database"))    

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.named<Test>("test") {
    enabled = false
}

tasks.register<Test>("workflowEndToEndTest") {
    description = "Run end-to-end integration tests."
    group = "verification"

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}
