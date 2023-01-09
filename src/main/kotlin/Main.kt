import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
//    println("Program arguments: ${args.joinToString()}")
    var out = ""
    args.forEach{out += " ${it}"}
    println(out)
    val path = System.getProperty("user.dir")
    println(path)
//    Runtime.getRuntime().exec("mycommand.sh")
//    ProcessBuilder("cmd /C git status".split(" "))
//        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
//        .start()
//        .waitFor()
    val cmd = "cmd /C gradlew test --tests \"local.test.framework.TestClass2\"" //works
//    val cmd = "cmd /C cd $path && gradlew test --tests \"local.test.framework.TestClass2\""
//    val pb = ProcessBuilder("cmd /C git status".split(" "))
    val pb = ProcessBuilder(cmd.split(" "))
    val output: String = IOUtils.toString(pb.start().inputStream, StandardCharsets.UTF_8)
    println("output: $output")
//    gradle run --args "aa bbb cc"
}
