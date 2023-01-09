package local.test.framework

import org.testng.annotations.*
import java.lang.reflect.Method

open class TestBase {
    @BeforeClass
    fun beforeClass(){
        println("before class body ${this.javaClass.name}")
    }
    @AfterClass
    fun afterClass(){
        println("after class body ${this.javaClass.name}")
    }
    @BeforeTest
    fun beforeTest(){
        println("before test body ${this.javaClass.name}")
    }
    @AfterTest
    fun afterTest(){
        println("after test body ${this.javaClass.name}")
    }
    @BeforeMethod
    fun beforeMethod(method: Method){
        println("before method name: '${method.name}'")
    }
    @AfterMethod
    fun afterMethod(){
        println("after method body ")
    }
}