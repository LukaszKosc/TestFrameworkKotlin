package local.test.framework.tests

import local.test.framework.TestBase
import org.testng.annotations.Test


class TestClass2 : TestBase(){

    @Test(groups = ["group1"])
    fun my_test21(){
        println("my_test21 body")
        1/0
    }

    @Test(groups = ["group2"])
    fun my_test22(){
        println("my_test22 body")
    }
}