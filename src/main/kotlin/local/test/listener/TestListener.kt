package local.test.listener

import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestResult


open class TestListener : ITestListener{

    private val contexts: ThreadLocal<ITestContext> = ThreadLocal<ITestContext>()
    private val testResults: ThreadLocal<ITestResult> = ThreadLocal<ITestResult>()
    override fun onStart(context: ITestContext?) {
        println("onStart method started")
    }

    override fun onFinish(context: ITestContext?) {
        println("onFinish method started")
    }
    override fun onTestStart(result: ITestResult) {
        println("onTestStart Method" + result.name)
        testResults.set(result)
        contexts.set(testResults.get().testContext)
        testMethodLogStatus("() started")
    }
    override fun onTestSuccess(result: ITestResult) {
        println("onTestSuccess Method" + result.name)
    }

    override fun onTestFailure(result: ITestResult) {
        println("onTestFailure Method" + result.name)
    }

    override fun onTestSkipped(result: ITestResult) {
        println("onTestSkipped Method" + result.name)
    }

    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult) {
        println("onTestFailedButWithinSuccessPercentage" + result.name)
    }
    private fun testMethodLogStatus(methodInfo: String) {
        println("cos loguje")
        println(
            """
            
            ${Thread.currentThread()}: ${testResults.get().testClass.realClass.name}.${testResults.get().method.methodName}$methodInfo
            """.trimIndent()
        )
    }
}