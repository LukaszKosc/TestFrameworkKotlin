package local.test.framework


import org.codehaus.classworlds.Launcher
import org.testng.annotations.Test
import java.io.File
import java.net.URL
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

fun main(args: Array<String>) {
    println("test main")
    ClassHelper.getClassesDescription()
}


class ClassHelper {

    companion object{

        fun findClasses(pckgname: String): List<Object> {
            var classesList: MutableList<Object> = mutableListOf()
            println("package: $pckgname")
            // Translate the package name into an absolute path
            var name = pckgname
            if (!name.startsWith("/")) {
                name = "/$name"
            }
            name = name.replace('.', '/')
            println("name: $name")
            // Get a File object for the package
            val url: URL = Launcher::class.java.getResource(name)
            val directory = File(url.getFile())

            println("Finding classes:")
            if (directory.exists()) {
                println("directory exists")
                // Get the list of the files contained in the package
                directory.walk()
                    .filter { f -> f.isFile() && f.name.contains('$') == false && f.name.endsWith(".class") }
                    .forEach {
                        val fullyQualifiedClassName = pckgname +
                                it.canonicalPath.removePrefix(directory.canonicalPath)
                                    .dropLast(6) // remove .class
                                    .replace('/', '.')
                                    .replace('\\', '.')
                        try {
                            // Try to create an instance of the object
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
            }else {
                println("no such path")
            }
            return classesList
        }
        fun getClassesDescription(clazzList: List<Object> = findClasses("local.test.framework.tests")){
//            val clazzListOutput: List<Map<String, Map<String, String>>>
            clazzList.forEach {
                val className = it.`class`.name
                println("className: $className")
                if (className.contains("TestClass")){
                    it::class.declaredMembers.forEach {
                        method -> run {
                            println("method: ${method.name}")
                            if(method.hasAnnotation<Test>()){
                                val testAnnotation = method.findAnnotation<Test>()
                                println("testAnnotation: $testAnnotation")
                                if (null != testAnnotation) {
                                    val groups: Array<String> = testAnnotation.groups
                                    if (groups != null) {
                                        if (groups.isNotEmpty()) {
                                            groups.forEach { group -> println("group: $group") }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

