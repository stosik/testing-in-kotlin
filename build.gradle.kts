import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
    groovy
}

group = "pl.stosik.testing"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")

    //Logging
    implementation("org.slf4j:slf4j-log4j12:2.0.5")

    //Spock
    testImplementation("org.spockframework:spock-core:2.4-M1-groovy-4.0")
    testImplementation("com.athaydes:spock-reports:2.4.0-groovy-4.0")
    testImplementation("org.apache.groovy:groovy-all:4.0.13")
    testImplementation("com.github.stefanbirkner:system-rules:1.19.0")

    //Spek
    testImplementation("org.jetbrains.spek:spek-api:1.1.5")
    testImplementation("org.jetbrains.spek:spek-junit-platform-engine:1.1.5")
    testImplementation("io.mockk:mockk:1.13.4")

    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()

    testClassesDirs = files(
        setOf(
            project.sourceSets.getByName("test").output.classesDirs,
            project.sourceSets.getByName("test").output.resourcesDir
        )
    )
    classpath += files(testClassesDirs)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
