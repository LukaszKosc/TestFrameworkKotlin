package local.test.framework

import org.apache.commons.io.FileUtils
import org.testng.TestNG
import org.testng.annotations.Test
import org.testng.xml.*
import java.io.File
import java.io.IOException
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.logging.Logger
import kotlin.collections.ArrayList
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

var logger: Logger = Logger.getLogger("Runner")

open class Runner {
    companion object {

        fun getAdvancedGroupFiltering(): XmlMethodSelector {
            val xmlSelector = XmlMethodSelector()
            var xmlScript = XmlScript()
            /*
                Possible tags:
                Type of tests
                 - api,
                 - gui,
                 - db,
                 - integration,
                 - ...
                Speed/length/resource consumption/etc
                 - quick,
                 - slow,
                 - performance,
                 - smoke,
                 - highresource,
                 - lowresource,
                 - ...

               Possible groups filters:
               - AND: tag1[ANDtag2]+           | INCLUDE/EXCLUDE equivalent or try to get the same
                 examples:                     |
                   - apiANDquick               | INCLUDE(api,quick) EXCLUDE(db,integration,db,slow,smoke,performance,...)
                   - dbANDquickANDhighresource | INCLUDE(db,quick,highresource) EXCLUDE(api,gui,lowresource,integration,slow,smoke,performance,...)
                - OR: tag1[ORtag2]+            |
                 examples:                     |
                   - apiORgui                  | INCLUDE(api,gui)
                   - apiORguiORdb              | INCLUDE(api,gui,db)
                - combined: OR + AND + NOT     | INCLUDE/EXCLUDE
                 examples:                     |
                   - (apiORgui)ANDquick        | INCLUDE(api,gui,quick) EXCLUDE(db,integration,slow,performance,smoke,...)
                                               | if only use INCLUDE(api,gui,quick) we could have tests which have tags:
                                               | db, integration - because if 'quick' is in those - then they will be included
                   - (apiORguiORdb)ANDslow     | INCLUDE(api,gui,slow) EXCLUDE(quick,integration,performance,smoke,...)
                                               | if only use INCLUDE(api,gui,slow) we could have tests which have tags:
                                               | db, integration - because if 'slow' is in those - then they will be included
            */
            xmlScript.language = "beanshell"
            xmlScript.expression = """
                suiteName = System.getProperty("groupToRun");
                //print(groups.toString() + " value: " + suiteName.toString() + " present?: " + groups.containsKey(suiteName).toString());
                groups.containsKey(suiteName);
                //mygroups = suiteName.split("AND");
                //groups.containsKey(mygroups[0]) && groups.containsKey(mygroups[1]);
            """.trimIndent()
            xmlSelector.script = xmlScript
            return xmlSelector
        }

        private fun getClassesToTest(jarFileName: String, testClassesPath: String): List<Object> {
            unpackJar(
//                "${System.getProperty("user.dir")}\\build\\libs\\TestFramework-1.0-SNAPSHOT.jar",
                "${System.getProperty("user.dir")}\\build\\libs\\$jarFileName",
                "${System.getProperty("user.dir")}\\",
               "$testClassesPath/"
            )
            return findClasses(testClassesPath.replace("/", "."))
        }

        fun runTestNG() {
            val testClassPathToExtract = "local/test/framework/tests"
            val jarFileToExtract = "TestFramework-1.0-SNAPSHOT.jar"
            val testNgRunner = TestNG()

            //building testng.xml file - start
            val xmlSuiteElement = XmlSuite()
            testNgRunner.setUseDefaultListeners(false)
            xmlSuiteElement.name = "Test Suite"

            //TODO: select which methods (based on groups, using AND, OR, NOT logical operators) - WIP
//            xmlSuiteElement.methodSelectors.add(getAdvancedGroupFiltering())
//            xmlSuiteElement.addListener("local.local.test.TestListener")

            xmlSuiteElement.addListener("org.uncommons.reportng.JUnitXMLReporter")
            val xmlTestElement = XmlTest(xmlSuiteElement)
            xmlTestElement.name = "Sample Test"

            //gather test classes to execute
            val testClassToExecuteList: MutableList<XmlClass> = ArrayList<XmlClass>().toMutableList()

            val classes = getClassesToTest(jarFileToExtract, testClassPathToExtract)
            classes.forEach { testClassToExecuteList += XmlClass(it.`class`) }
            xmlTestElement.xmlClasses = testClassToExecuteList

            val testsToExecuteList: MutableList<XmlTest> = ArrayList<XmlTest>().toMutableList()
            testsToExecuteList.add(xmlTestElement)
            println("before tests")
            testsToExecuteList.forEach { println("${it.name}") }
            println("after tests")
            xmlSuiteElement.tests = testsToExecuteList

            //add suites to xml file
            val suitesToExecuteList: MutableList<XmlSuite> = ArrayList<XmlSuite>().toMutableList()
            suitesToExecuteList.add(xmlSuiteElement)
            testNgRunner.setXmlSuites(suitesToExecuteList)
            //building testng.xml file - finish

//            testNgRunner.setGroups("GUI")
//            testNgRunner.setExcludedGroups("slow")

//            testNgRunner.setVerbose(10)
            logger.info { "Tests Execution started" }
            testNgRunner.run()
            logger.info { "Tests Execution finished" }
        }

        private fun unpackJar(jarFilePath: String, targetDir: String, testClassPath: String) {
            try {
                val userDir = File(targetDir)
                userDir.mkdir()
                logger.info { "jarFilePath: '$jarFilePath'." }
                val jar = JarFile(jarFilePath)
                jar.stream()
                    .filter { file: JarEntry -> file.name.contains(testClassPath) }
                    .peek { file: JarEntry ->
                        val res = File(userDir.path + "/" + file.name)
                        if (file.isDirectory) {
                            res.mkdirs()
                        } else {
                            try {
                                FileUtils.copyInputStreamToFile(jar.getInputStream(file), res)
                            } catch (e: IOException) {
                                println(e.message)
                            }
                        }
                    }.forEach { file: JarEntry -> logger.info { "Extracted file 'file.name'." } }
            } catch (e: IOException) {
                throw Exception("Unable to open jar '$jarFilePath'. " + e.message)
            }
        }

        private fun removeUnpackedJarContent() {
            val directory = File("local\\")

            if (directory.exists()) {
                directory.deleteRecursively()
                if (!directory.exists()) {
                    logger.info { "Directory '$directory' deleted." }
                }
            }
        }

        private fun findClasses(packageName: String): List<Object> {
            var classesList: MutableList<Object> = mutableListOf()
            println("package '$packageName'.")
            // Translate the package name into an absolute path
            var name = packageName
            if (!name.startsWith("/")) {
                name = "/$name"
            }
            name = name.replace('.', '/')
            println("name '$name'.")
            // Get a File object for the package
//            val url: URL = Launcher::class.java.getResource(name)
//            val directory = File(url.getFile())
            val directory = File("./$name")

            println("Finding classes in directory '$directory'.")
            if (directory.exists()) {
                // Get the list of the files contained in the package
                directory.walk()
                    .filter { f -> f.isFile() && f.name.contains('$') == false && f.name.endsWith(".class") }
                    .forEach {
//                        println("pckgname: $pckgname")
                        val fullyQualifiedClassName = packageName +
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
                println("not found '$directory' path.")
            }
//            removeUnpackedJarContent()
            return classesList
        }

        fun getClassesDescription(clazzList: List<Object> = findClasses("local.test.framework.tests")): List<Object> {
            var clazzDesc: MutableList<Map<String, MutableList<Map<String, Any>>>> =
                mutableListOf<Map<String, MutableList<Map<String, Any>>>>().toMutableList()
            clazzList.forEach { clazz ->
                val className = clazz.`class`.name
                if (className.contains("TestClass")) {
                    var myListOfMethods: MutableList<Map<String, Any>> = mutableListOf()
                    clazz::class.declaredMembers.forEach { method ->
                        run {
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
                                    myListOfMethods.add(
                                        mapOf(
                                            "method" to method.name,
//                                        "custom" to testAnnotation.attributes[0],
                                            "custom" to customAttrs,
                                            "priority" to testAnnotation.priority,
                                            "description" to testAnnotation.description,
                                            "alwaysRun" to testAnnotation.alwaysRun,
                                            "testName" to testAnnotation.testName,
                                            "suiteName" to testAnnotation.suiteName,
                                            "groups" to groupsList.dropLast(1),
                                            "enabled" to testAnnotation.enabled
                                        )
                                    )
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
                    it.entries.forEach { xxx -> logger.info { "entries: ${xxx}}" } }
                }
            }
            return clazzList
        }
    }

}