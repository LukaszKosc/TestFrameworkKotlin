import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
}

group = "local.test"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.testng:testng:7.1.0")
    implementation("org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7")
}

tasks.test {
    testLogging {
        outputs.upToDateWhen {false}
        showStandardStreams = true
    }
    useTestNG()
//    useTestNG() {
//        testLogging.showStandardStreams = true
//    }

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}