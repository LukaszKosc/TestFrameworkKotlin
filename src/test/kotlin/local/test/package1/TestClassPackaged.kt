package local.test.package1

import local.test.framework.TestBase
import org.testng.annotations.Test

class TestClassPackaged : TestBase(){

    @Test
    fun my_test(){
        println("some body")
    }

    @Test
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