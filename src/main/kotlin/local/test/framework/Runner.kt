package local.test.framework

import org.apache.commons.io.FileUtils
import org.testng.TestNG
import org.testng.annotations.Test
import org.testng.xml.XmlClass
import org.testng.xml.XmlMethodSelector
import org.testng.xml.XmlSuite
import org.testng.xml.XmlTest
import java.io.File
import java.io.IOException
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.logging.Logger
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

var logger: Logger = Logger.getLogger("Runner")

open class Runner {
    companion object {

        fun runTestNG() {
//            var cp = System.getProperty("java.class.path")
////            println("before: $cp")
//            cp += ";C:\\Users\\Kostek\\IdeaProjects\\TestFramework\\build\\classes\\kotlin\\test"
//            System.setProperty("java.class.path", cp)
////            println("after '${System.getProperty("java.class.path")}'.")
//            println(System.getProperty("user.dir"))
            logger!!.info { "This is my first log4j's statement" }
            val targetDir = ".\\"
            unpackJar(
                "C:\\Users\\Kostek\\IdeaProjects\\TestFramework\\libs\\TestFramework-1.0-SNAPSHOT.jar",
                targetDir
            )
            val classes = getClassesDescription()
            removeLocalFiles()

//            classes.forEach { logger.info {"got class: '${it}'"} }
            classes.forEach { logger.info {"got class: '${it.`class`}'"} }

            var myTestNG: TestNG = TestNG()
            var mySuite: XmlSuite = XmlSuite()

            myTestNG.setUseDefaultListeners(false)
            mySuite.setName("Sample Suite")
//            mySuite.addListener("local.local.test.TestListener");
            mySuite.addListener("org.uncommons.reportng.JUnitXMLReporter");
            var myTest: XmlTest = XmlTest(mySuite);
            myTest.setName("Sample Test");
            val myClasses: MutableList<XmlClass> = ArrayList<XmlClass>().toMutableList()

            classes.forEach { myClasses += XmlClass(it.`class`) }
//            myClasses += XmlClass("local.test.framework.tests.TestClass")
//            myClasses += XmlClass("local.test.framework.tests.TestClass2")
//
            myTest.xmlClasses = myClasses;
            var myTests: MutableList<XmlTest> = ArrayList<XmlTest>().toMutableList()

            myTests.add(myTest);
//            myTests.forEach { xx ->
//                run {
//                    println("each element: ${xx.classes}")
//                    xx.classes.forEach { yy -> run {
//                        yy.setParameters(mapOf("enable" to "false"))
//                        println("clasa: "+ yy.name) } // clasa: local.test.framework.tests.TestClass
//                    }
//                }
//            }
            println("before tests")
            myTests.forEach { println("${it.name}") }
            println("after tests")
            mySuite.setTests(myTests);
            var mySuites: MutableList<XmlSuite> = ArrayList<XmlSuite>().toMutableList()
            mySuites.add(mySuite);
            myTestNG.setXmlSuites(mySuites);
//            myTestNG.setGroups("grupa5")
//            myTestNG.setGroups("grupa3")
//            myTestNG.setGroups("grupa5,grupa3")
//            myTestNG.setExcludedGroups("gr1")
            myTestNG.setVerbose(10)
            myTestNG.run()

        }

        fun unpackJar(jarFilePath: String, targetDir: String) {
            try {
                val userDir = File(targetDir)
                userDir.mkdir()
                val jar = JarFile(jarFilePath)
                jar.stream()
                    .filter { file: JarEntry ->
                        file.name.contains("local/test/framework/tests/")
                    }
                    .peek { file: JarEntry ->
                        val res =
                            File(userDir.path + "/" + file.name)
                        if (file.isDirectory) {
                            res.mkdirs()
                        } else {
                            try {
                                FileUtils.copyInputStreamToFile(jar.getInputStream(file), res)
                            } catch (e: IOException) {
                                println(e.message)
                            }
                        }
                    }.forEach { file: JarEntry -> logger.info { "Extracted file: " + file.name } }
            } catch (e: IOException) {
                throw Exception(
                    java.lang.String.format(
                        "Unable to open jar %s", jarFilePath
                    ), e
                )
            }
        }

        fun removeLocalFiles() {
            val directory = File("local\\")

            if (directory.exists()) {
                directory.deleteRecursively()
            }
            if (!directory.exists()) {
                logger.info { "Directory '$directory' deleted." }

            }
        }

        fun findClasses(pckgname: String): List<Object> {
            var classesList: MutableList<Object> = mutableListOf()
//            println("package: $pckgname")
            // Translate the package name into an absolute path
            var name = pckgname
            if (!name.startsWith("/")) {
                name = "/$name"
            }
            name = name.replace('.', '/')
//            println("name: $name")
            // Get a File object for the package
//            val url: URL = Launcher::class.java.getResource(name)
//            val directory = File(url.getFile())
            val directory = File("./$name")

//            println("Finding classes:")
            if (directory.exists()) {
//                println("directory exists")
                // Get the list of the files contained in the package
                directory.walk()
                    .filter { f -> f.isFile() && f.name.contains('$') == false && f.name.endsWith(".class") }
                    .forEach {
//                        println("pckgname: $pckgname")
                        val fullyQualifiedClassName = pckgname +
                                it.canonicalPath.removePrefix(directory.canonicalPath)
                                    .dropLast(6) // remove .class
                                    .replace('/', '.')
                                    .replace('\\', '.')
                        try {
                            // Try to create an instance of the object
//                            println("fullyQualifiedClassName: $fullyQualifiedClassName")
                            val o = Class.forName(fullyQualifiedClassName).getDeclaredConstructor().newInstance()
                            if (o is Object) {
                                classesList += o
                            }
                        } catch (cnfex: ClassNotFoundException) {
                            System.err.println(cnfex)
                        } catch (iex: InstantiationException) {
                            // We try to instantiate an interface
                            // or an object that does not have a
                            // default constructor
                        } catch (iaex: IllegalAccessException) {
                            // The class is not public
                        }
                    }
            } else {
                println("no such path")
            }
            return classesList
        }

        fun getClassesDescription(clazzList: List<Object> = findClasses("local.test.framework.tests")): List<Object> {
            val clazzesList: MutableList<Map<String, Map<String, Any>>> =
                mutableListOf<Map<String, Map<String, Any>>>().toMutableList()
            var clazzDesc: MutableList<Map<String, MutableList<Map<String, Any>>>> =
                mutableListOf<Map<String, MutableList<Map<String, Any>>>>().toMutableList()

            val outputClazzList: MutableList<Object> = mutableListOf<Object>().toMutableList()
            clazzList.forEach { clazz ->

                val className = clazz.`class`.name

//                println("className: $className")
                if (className.contains("TestClass")) {
//                    println("className: $className")

                    var myListOfMethods: MutableList<Map<String, Any>> = mutableListOf()
//                    clazzesList += clazzDesc
                    clazz::class.declaredMembers.forEach { method ->
                        run {
//                        clazzDesc[className] = clazzDesc[className]!! + mapOf("method" to method.name)
//                            println("method: ${method.name}")
//                            clazzDesc.add(mutableMapOf("className" to className))


                            if (method.hasAnnotation<Test>()) {
                                val testAnnotation = method.findAnnotation<Test>()
//                                println("testAnnotation: $testAnnotation")
                                if (null != testAnnotation) {
                                    var groupsList = ""
                                    if (testAnnotation.groups.isNotEmpty())
                                        testAnnotation.groups.forEach { group -> groupsList += "$group," }
                                    var customAttrs = mutableListOf<Map<String, Any>>()
                                    testAnnotation.attributes.iterator().forEach {
                                        var arr = mutableListOf<String>()
                                        it.values.forEach { xx -> arr.add(xx) }
                                        customAttrs.add(mapOf(it.name to arr))
                                    }
//                                    Below if verifies if class contains method with customized attributes
//                                    Ultimate idea here is to get&run via TestNG only methods with 'tags' or any other indicator defined via command line
//                                    Currently I will disable it
//                                    if (customAttrs.size > 0){
//                                        outputClazzList += clazz
//                                    }
                                    myListOfMethods.add(mapOf(
                                        "method" to method.name,
//                                        "custom" to testAnnotation.attributes[0],
                                        "custom" to customAttrs,
                                        "priority" to testAnnotation.priority,
                                        "description" to testAnnotation.description,
                                        "alwaysRun" to testAnnotation.alwaysRun,
                                        "testName" to testAnnotation.testName,
                                        "suiteName" to testAnnotation.suiteName,
                                        "groups" to groupsList.dropLast(1),
                                        "enabled" to testAnnotation.enabled))
                                }
                            }
                        }
                    }
                    clazzDesc.add(
                        mapOf(
                            className to myListOfMethods
                        )
                    )
                }
            }
            clazzDesc.forEach {
                run {
                    logger.info { "class description: ${it.entries}" }
                    it.entries.forEach { xxx-> logger.info { "entries: ${xxx}}" }}
                }
            }
            return clazzList
//            return outputClazzList
        }
    }

}