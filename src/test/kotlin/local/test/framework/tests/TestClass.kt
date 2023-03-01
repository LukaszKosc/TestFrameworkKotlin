package local.test.framework.tests

import local.test.framework.TestBase
import org.testng.annotations.CustomAttribute
import org.testng.annotations.Test

class TestClass : TestBase(){

    @Test(groups=["GUI", "quick","grupa5"], alwaysRun = true)
    fun b1my_test1(){
        println("my_test1")
    }

    @Test(groups=["GUI","slow"], alwaysRun = true)
    fun b1my_test111(){
        println("my_test111")
    }

//    @Test(groups=["gru1", "grupa2", "grupa3"], testName = "test1234", alwaysRun = true, timeOut = 500L, /*dependsOnMethods= arrayOf("b1my_test1"),*/ description = "tags")
    @Test(groups=["GUI", "quick", "grupa3"], testName = "test1234", alwaysRun = true, timeOut = 500L, /*dependsOnMethods= arrayOf("b1my_test1"),*/ description = "dup[a",
        attributes = [
            CustomAttribute(name = "tags", values = arrayOf("tag123", "tag2")),
            CustomAttribute(name = "someAttr", values = arrayOf("attr1", "attr2"))]
    )
    fun a1my_test2(){
        println("some body of AND used 123")
        Thread.sleep(505)
//    1/0
    }

    @Test(enabled = true)
    fun my_test3(){
        println("somea body")
        1/0
    }

    @Test(groups=["API", "quick", "grupa5"], priority = 1)
    fun my_test4(){
        println("some bodysss")
    }
}