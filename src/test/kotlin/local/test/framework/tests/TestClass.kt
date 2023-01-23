package local.test.framework.tests

import local.test.framework.TestBase
import org.testng.annotations.Test

class TestClass : TestBase(){

    @Test
    fun my_test(){
        println("changed body - after some change - hahaha")
    }

    @Test(groups=["gr1", "grupa2"], testName = "test123")
    fun my_test2(){
        println("some body")
    }

    @Test
    fun my_test3(){
        println("somea body")
    }

    @Test
    fun my_test4(){
        println("some bodysss")
    }
}