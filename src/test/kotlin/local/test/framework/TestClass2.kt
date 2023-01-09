package local.test.framework

import org.testng.annotations.Test
import org.testng.xml.dom.Tag


class TestClass2 : TestBase(){

    @Tag(name = "parity")
    @Test
    fun my_test11(){
        println("my_test11 body")
    }

    @Tag(name = "parity")
    @Test
    fun my_test12(){
        println("my_test12 body")
    }

    @Test
    fun my_test13(){
        println("my_test13 body")
    }

    @Test
    fun my_test14(){
        println("my_test14 bodysss")
    }

    @Test
    fun my_test15(){
        println("my_test15 bodysss")
    }
}