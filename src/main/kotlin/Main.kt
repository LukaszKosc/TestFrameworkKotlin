import local.test.framework.Runner

//import local.local.test.TestSomething

fun main(args: Array<String>) {
    println("Hello World!")
//    val x: TestSomething = TestSomething()
//    x.p = "asdf"
//    println(x.p)

//    var cp = System.getProperty("java.class.path")
//    println("before: $cp")
//    cp = "C:\\Users\\Kostek\\IdeaProjects\\TestFramework\\build\\classes\\kotlin\\test;$cp"
//    System.setProperty("java.class.path", cp)
//    println("after: '${System.getProperty("java.class.path")}'.")
//
//    try {
//        Class.forName("local.test.package1.TestClassPackaged")
//        Class.forName("local.test.framework.TestClass2")
//        Class.forName("local.test.frasmework.TestClass34")
//        // it exists on the classpath
//    } catch (e: ClassNotFoundException) {
//        // it does not exist on the classpath
//        println("err: $e")
//    }
//    val myClasses: MutableList<XmlClass> = ArrayList<XmlClass>().toMutableList()
//    myClasses += XmlClass("local.test.package1.TestClassPackaged")
//    XmlClass("local.test.package1.TestClassPackaged")



    Runner.runTestNG()

//
//    // Try adding program arguments via Run/Debug configuration.
//    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
////    println("Program arguments: ${args.joinToString()}")
//    var out = ""
//    args.forEach{out += " ${it}"}
//    println(out)
//    val path = System.getProperty("user.dir")
//    println(path)
//
//
////    Runtime.getRuntime().exec("mycommand.sh")
////    ProcessBuilder("cmd /C git status".split(" "))
////        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
////        .start()
////        .waitFor()
//
//
//    val cmd = "cmd /C gradlew test --tests \"local.test.framework.TestClass2\"" //works
////    val cmd = "cmd /C cd $path && gradlew test --tests \"local.test.framework.TestClass2\""
////    val pb = ProcessBuilder("cmd /C git status".split(" "))
//    val pb = ProcessBuilder(cmd.split(" "))
//    val output: String = IOUtils.toString(pb.start().inputStream, StandardCharsets.UTF_8)
//    println("output: $output")
////    gradle run --args "aa bbb cc"
}
