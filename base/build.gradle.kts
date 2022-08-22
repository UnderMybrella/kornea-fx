import dev.brella.kornea.gradle.kotlinxCoroutinesModule
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
    id("dev.brella.kornea")
}

group = "dev.brella"
version = "1.0.0-alpha"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlinxCoroutinesModule("core"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

javafx {
    version = "17.0.1"
    modules = listOf("javafx.base")
    configuration = "implementation"
}