package com.example.adviceapp

import com.example.adviceapp.View.LogInScreen
import com.example.adviceapp.View.PostItem
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val sut = LogInScreen()
    val sut1 = PostItem()


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkUserInput() {
        assert(checkForNoEmptyInput())
    }

    fun checkForNoEmptyInput(): Boolean {
        if (sut.userLoggedInCheck("", "") == false) {
            return true
        } else {
            return false
        }
    }

}

