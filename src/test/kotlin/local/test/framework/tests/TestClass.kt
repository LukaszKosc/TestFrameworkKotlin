package local.test.framework.tests

import local.test.framework.TestBase
import org.testng.annotations.Test

class TestClass : TestBase(){

    @Test(groups=["grupa2", "grupa3","grupa5"], alwaysRun = true)
    fun my_test1(){
        println("my_test1")
        1/0
    }

    @Test(groups=["gr1", "grupa2", "grupa3"], testName = "test1234", alwaysRun = true)
    fun my_test2(){
        println("some body of AND used 123")
    }

    @Test(enabled = false)
    fun my_test3(){
        println("somea body")
    }

    @Test(groups=["gr1", "grupa3", "grupa5"], priority = 1)
    fun my_test4(){
        println("some bodysss")
    }
}