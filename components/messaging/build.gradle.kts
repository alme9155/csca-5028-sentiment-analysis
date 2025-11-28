plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "cu.csca5028.alme9155.messaging"
version = "1.0.0"

dependencies {
    implementation(project(":support:logging-support"))
    implementation(project(":components:database"))
    implementation(project(":components:sentiment"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.rabbitmq:amqp-client:5.21.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
    testImplementation("io.mockk:mockk:1.13.14")    
}
kotlin {
    jvmToolchain(21)
}