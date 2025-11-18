plugins {
    kotlin("jvm")
    application
}

group = "cu.csca5028.alme9155.web"

val ktorVersion: String by project

dependencies {
    implementation(project(":components:data-analyzer"))
    implementation(project(":support:logging-support"))
    implementation(project(":support:workflow-support"))
    implementation("org.slf4j:slf4j-nop:2.0.16")

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-freemarker-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktorVersion")
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