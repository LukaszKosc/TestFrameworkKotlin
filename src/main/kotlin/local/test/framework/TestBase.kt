package local.test.framework

import org.testng.Reporter
import org.testng.annotations.*
import org.testng.log4testng.Logger
import java.lang.reflect.Method

open class TestBase(){
    companion object{
//        val LOGGER = Logger.getLogger(TestBase::class.java)
        fun log(logMsg: String){
            Reporter.log(logMsg)
        }
    }
    @BeforeClass(alwaysRun = true)
    fun beforeClass(){
        println("before class body ${this.javaClass.name}")
        log("msg logged from BeforeClass")
    }
    @AfterClass(alwaysRun = true)
    fun afterClass(){
        println("after class body ${this.javaClass.name}")
        log("msg logged from AfterClass")
    }
    @BeforeTest(alwaysRun = true)
    fun beforeTest(){
        println("before test body ${this.javaClass.name}")
        log("msg logged from BeforeTest")
    }
    @AfterTest(alwaysRun = true)
    fun afterTest(){
        println("after test bod123y ${this.javaClass.name}")
        log("msg logged from AfterTest")
    }
    @BeforeMethod(alwaysRun = true)
    fun beforeMethod(method: Method){
        println("before method name: '${method.name}'.")
        log("msg logged from BeforeMethod")
    }
    @AfterMethod(alwaysRun = true)
    fun afterMethod(method: Method){
        println("after method name: '${method.name}'.")
        log("msg logged from AfterMethod")
    }
}
