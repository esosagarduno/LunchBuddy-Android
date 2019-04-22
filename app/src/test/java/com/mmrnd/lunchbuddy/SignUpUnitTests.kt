package com.mmrnd.lunchbuddy

import org.junit.Assert
import org.junit.Test

/**
 * Created by Esteban Sosa
 */

class SignUpUnitTests {

    val activity = SignUpActivity()

    @Test
    fun form_isValid() {
        // Name
        Assert.assertEquals(activity.validateName(""), false)
        Assert.assertEquals(activity.validateName("Esteban"), true)
        // Email
        //Assert.assertEquals(activity.validateEmail("a@gmail.com"), true)
        // Email match
        Assert.assertEquals(activity.validateEmailMatch("", ""), true)
        Assert.assertEquals(activity.validateEmailMatch("a@gmail.com", "a@gmail.com"), true)
        // Password
        Assert.assertEquals(activity.validatePassword(""), false)
        Assert.assertEquals(activity.validatePassword("123"), false)
        Assert.assertEquals(activity.validatePassword("12345678"), true)
        // Password match
        Assert.assertEquals(activity.validatePasswordMatch("", ""), true)
        Assert.assertEquals(activity.validatePasswordMatch("12345678", "12345678"), true)
    }
}