import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
}

group = "local.test"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    flatDir {
        dirs("libs")
    }
}
sourceSets{
    val main by getting
    val test by getting
    val all by creating{
        java {
            srcDir(arrayOf("src/main/kotlin", "src/test/kotlin"))
            compileClasspath += main.output + test.output
        }
    }

}
dependencies {

    implementation("org.testng:testng:7.7.0")
    implementation("org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")
    runtimeOnly(":TestFramework:1.0-SNAPSHOT")
//    implementation(":tests.local")
}

tasks.test {
    testLogging {
        outputs.upToDateWhen {false}
        showStandardStreams = true
    }
    useTestNG() {
        doFirst {
            // gradle testClasses //compiles tests but do not run them!!!
//            classpath = files("$buildDir/classes/kotlin/main", "$buildDir/classes/kotlin/test")
//            println("classpath: $classpath")
//            classpath.forEach { if (it.toString().contains("\\test")) println(it) }
        }
//        val args = []
//        if(project.hasProperty("defaultListeners")) {
//            args += ["-usedefaultlisteners", "true"]
//        } else {
//            args += ["-usedefaultlisteners", "false",
//                "-listener", "testng.listeners.TestExecListener"]
//        }


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
//        println("tags: '$tags'.")
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
//    archiveFileName.set("appTest-${project.version}.jar")
    from(sourceSets.test.get().output)
    manifest {
        attributes(mapOf("Main-Class" to "local.test.framework.HelperKt",
            "Implementation-Title" to "Gradle Test Classes",
            "Implementation-Version" to "1.0-SNAPSHOT"))
    }
    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all of the dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
//    dependsOn(configurations.compileClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

//
tasks.register<Jar>("appJar") {
//    archiveFileName.set("appMain-${project.version}.jar")
    from(sourceSets.main.get().output)
    manifest {
        attributes(mapOf("Main-Class" to "MainKt",
            "Implementation-Title" to "Gradle App Classes",
            "Implementation-Version" to "1.0-SNAPSHOT"))
    }

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

//tasks.register<Jar>("testJar") {
////    from(sourceSets.main.get().output)
//    from("kotlin/tests/local/"){
//        include("**")
//    }
//}

//tasks.jar {
//    from(sourceSets["test"].output)
//}

//
//tasks.create("args_eater"){
//    println("hello from: '"+this.name+"'.")
//    val songName = properties["tags"]
//        ?: System.getenv("TAGS")
//        ?: error("""Property "tags" or environment variable "TAGS" not found""")
//    println("Value: $songName")
//    doLast{
//        println("test msg")
//        if (project.hasProperty("args")) {
//            println("Our input argument with project property ["+project.name+"]")
//        }
//
//    }
//}
//
//task("getTags"){
//    println("hello from: '"+this.name+"'.")
//    val songName = properties["tags"]
//        ?: System.getenv("TAGS")
//        ?: error("""Property "tags" or environment variable "TAGS" not found""")
//    println("Value: $songName")
//}