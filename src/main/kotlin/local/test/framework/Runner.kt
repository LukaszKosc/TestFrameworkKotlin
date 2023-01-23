package local.test.framework

import org.apache.commons.io.FileUtils
import org.codehaus.classworlds.Launcher

import org.testng.TestNG
import org.testng.annotations.Test
import org.testng.xml.XmlClass
import org.testng.xml.XmlSuite
import org.testng.xml.XmlTest
import java.io.File
import java.io.IOException
import java.net.URL
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

            classes.forEach { logger.info {"got class: '${it}'"} }

//            var myTestNG: TestNG = TestNG()
//            var mySuite: XmlSuite = XmlSuite()
//
//            myTestNG.setUseDefaultListeners(false)
//            mySuite.setName("Sample Suite")
////            mySuite.addListener("local.local.test.TestListener");
////            mySuite.addListener("org.uncommons.reportng.JUnitXMLReporter");
//            var myTest: XmlTest = XmlTest(mySuite);
//            myTest.setName("Sample Test");
//            val myClasses: MutableList<XmlClass> = ArrayList<XmlClass>().toMutableList()
//            myClasses += XmlClass("local.test.framework.tests.TestClass")
//            myClasses += XmlClass("local.test.framework.tests.TestClass2")
//
//            myTest.xmlClasses = myClasses;
//            var myTests: MutableList<XmlTest> = ArrayList<XmlTest>().toMutableList()
//            myTests.add(myTest);
//            mySuite.setTests(myTests);
//            var mySuites: MutableList<XmlSuite> = ArrayList<XmlSuite>().toMutableList()
//            mySuites.add(mySuite);
//            myTestNG.setXmlSuites(mySuites);
//            myTestNG.run();
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
            val clazzesList: MutableList<Map<String, Map<String, String>>> =
                mutableListOf<Map<String, Map<String, String>>>().toMutableList()
            var clazzDesc: MutableList<Map<String, MutableList<Map<String, String>>>> =
                mutableListOf<Map<String, MutableList<Map<String, String>>>>().toMutableList()
            clazzList.forEach {

                val className = it.`class`.name

//                println("className: $className")
                if (className.contains("TestClass")) {
//                    println("className: $className")

                    var myListOfMethods: MutableList<Map<String, String>> = mutableListOf()
//                    clazzesList += clazzDesc
                    it::class.declaredMembers.forEach { method ->
                        run {
//                        clazzDesc[className] = clazzDesc[className]!! + mapOf("method" to method.name)
//                            println("method: ${method.name}")
//                            clazzDesc.add(mutableMapOf("className" to className))


                            if (method.hasAnnotation<Test>()) {
                                val testAnnotation = method.findAnnotation<Test>()
//                                println("testAnnotation: $testAnnotation")
                                if (null != testAnnotation) {
                                    val groups: Array<String> = testAnnotation.groups
                                    var groupsList = ""
                                    if (groups != null) {
                                        if (groups.isNotEmpty()) {
                                            groups.forEach { group ->
                                                groupsList += "$group,"
//                                                println("group: $group")
                                            }
                                        }
                                    }
                                    groupsList = groupsList.dropLast(1)
                                    myListOfMethods.add(mapOf("method" to method.name, "groups" to groupsList))
                                }
                            }
                        }

//                    clazzesList += clazzDesc
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
                    logger.info { "got element: ${it.entries}" }
                }
            }
        return clazzList
        }
    }

}