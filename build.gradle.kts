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
sourceSets{
    val main by getting
    val test by getting
}
dependencies {
    implementation("org.testng:testng:7.7.0")
    implementation("org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")
    implementation("org.freemarker:freemarker:2.3.28")
    implementation(files("libs/reportng-1.2.2.jar"))
}

tasks.test {
    testLogging {
        outputs.upToDateWhen {false}
        showStandardStreams = true
    }
    useTestNG() {
        testLogging.showStandardStreams = true

        val parallelValue = properties["parallel"]
        if (parallelValue != null){
            val parallelismMethod = parallelValue.toString()
            if (parallelismMethod in listOf("methods", "tests", "classes", "instances")){
                println("Running tests using parallelism: '$parallelismMethod'.")
                parallel = parallelismMethod
            }else{
                println("No such type of parallelism like '$parallelismMethod'.")
            }
        }

        val tags = properties["tags"]
        if (tags != null) {
            includeGroups("$tags")
        }
//        val out = getOutputDirectory()
//        println("outputDir: $out")
//
//        val projDir = getProjectDir()
//        println("projectDir: $projDir")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

tasks.register<Jar>("testJar") {
    archiveFileName.set("appTest-${project.version}.jar")
    from(sourceSets.test.get().output)
}

tasks.register<Jar>("appJar") {
//    archiveFileName.set("appMain-${project.version}.jar")
    //sufficient appJar jar generation - line 'from(sourceSets.test.get().output)' provides classes from 'src/test/kotlin'
    from(sourceSets.main.get().output)
    manifest {
        attributes(mapOf("Main-Class" to "MainKt",
            "Implementation-Title" to "Gradle App Classes",
            "Implementation-Version" to "1.0-SNAPSHOT"))
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.test.get().output)
    dependsOn(configurations.runtimeClasspath)
    dependsOn(configurations.compileClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
