package local.test.framework

import org.codehaus.classworlds.Launcher
import org.testng.TestNG
import org.testng.xml.XmlClass
import org.testng.xml.XmlSuite
import org.testng.xml.XmlTest
import java.io.File
import java.net.URL

open class Runner {
    companion object {
        fun runTestNG() {
            var cp = System.getProperty("java.class.path")
//            println("before: $cp")
            cp += ";C:\\Users\\Kostek\\IdeaProjects\\TestFramework\\build\\classes\\kotlin\\test"
            System.setProperty("java.class.path", cp)
//            println("after '${System.getProperty("java.class.path")}'.")

//            findClasses("local.test.framework.test")
            println(System.getProperty("user.dir"))

            var myTestNG: TestNG = TestNG()
            var mySuite: XmlSuite = XmlSuite()

            myTestNG.setUseDefaultListeners(false)
            mySuite.setName("Sample Suite")
//            mySuite.addListener("local.local.test.TestListener");
////            mySuite.addListener("org.uncommons.reportng.JUnitXMLReporter");
            var myTest: XmlTest = XmlTest(mySuite);
            myTest.setName("Sample Test");
            val myClasses: MutableList<XmlClass> = ArrayList<XmlClass>().toMutableList()
//            myClasses += XmlClass("local.test.framework.test.TestClass")
            myClasses += XmlClass("local.test.framework.tests.TestClass")
            myClasses += XmlClass("local.test.framework.tests.TestClass2")



            myTest.xmlClasses = myClasses;
            var myTests: MutableList<XmlTest> = ArrayList<XmlTest>().toMutableList()
            myTests.add(myTest);
            mySuite.setTests(myTests);
            var mySuites: MutableList<XmlSuite> = ArrayList<XmlSuite>().toMutableList()
            mySuites.add(mySuite);
            myTestNG.setXmlSuites(mySuites);
            myTestNG.run();


//            //working code
//            var x: Class<*>? = null
//            lateinit var clazzX: Class<*>
//            val tla = TestListenerAdapter()
//            val testng = TestNG()
//            try{
//                x = Class.forName("local.test.framework.test.TestClass")
//            } catch (e: ClassNotFoundException) {
//                // it does not exist on the classpath
//                println("err: $e")
//            }
//            testng.setTestClasses(arrayOf(local.test.framework.test.TestClass::class.java))
//            testng.addListener(tla)
//            testng.run()

        }



//        fun listCodeClasses() {
//            val reflections = Reflections("my.project.prefix")
//
//            val allClasses: Set<Class<out Any?>> = reflections.getSubTypesOf(Any::class.java)
//
//
//            val pkg: String = TabA::class.java.getPackage().getName()
//            val relPath = pkg.replace('.', '/')
//
//            val resource = ClassLoader.getSystemClassLoader().getResource(relPath)
//                ?: throw RuntimeException(
//                    "Unexpected problem: No resource for "
//                            + relPath
//                )
//
//            val f = File(resource.path)
//
//            val files = f.list()
//
//            for (i in files.indices) {
//                val fileName = files[i]
//                var className: String? = null
//                var fileNm: String? = null
//                if (fileName.endsWith(".class")) {
//                    fileNm = fileName.substring(0, fileName.length - 6)
//                    className = "$pkg.$fileNm"
//                }
//                if (className != null) {
//                    if (!tabClasses.containsKey(className)) tabClasses.put(fileNm, className)
//                }
//            }
//        }
    }

}