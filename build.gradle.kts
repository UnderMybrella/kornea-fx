import dev.brella.kornea.gradle.defineVersions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10" apply false
    id("org.openjfx.javafxplugin") version "0.0.10" apply false
    id("dev.brella.kornea") version "1.3.0"
}

group = "dev.brella"

defineVersions {
    ktor("2.0.0")
    korneaBase("1.1.0-alpha")
    korneaErrors("3.1.0-alpha")
    kotlinxSerialisation("1.3.3")
    kotlinxCoroutines("1.6.2")

    "logback".."1.2.11"
}