package local.test.framework

import org.testng.annotations.*
import java.lang.reflect.Method


open class TestBase(){
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
        println("after test bod123y ${this.javaClass.name}")
    }
    @BeforeMethod
    fun beforeMethod(method: Method){
        println("before method name: '${method.name}'.")
    }
    @AfterMethod
    fun afterMethod(method: Method){
        println("after method name: '${method.name}'.")
    }
}
