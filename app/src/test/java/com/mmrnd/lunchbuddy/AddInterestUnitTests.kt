package com.mmrnd.lunchbuddy

import org.junit.Assert
import org.junit.Test

class AddInterestUnitTests {

    val activity = AddInterestActivity()

    @Test
    fun newInterest_isValid() {
        Assert.assertEquals(activity.validateInterest("Stuff"), activity.INTEREST_OK)
        Assert.assertEquals(activity.validateInterest(""), activity.INTEREST_INVALID)
        Assert.assertEquals(activity.validateInterest(  "Hello... "), activity.INTEREST_INVALID)
    }

    @Test
    fun formatInterest_isValid() {
        Assert.assertEquals(activity.formatInterest("  Hello "), "Hello")
        Assert.assertEquals(activity.formatInterest("...I like to swim...& "), "I like to swim")
        Assert.assertEquals(activity.formatInterest(""), "")
        Assert.assertEquals(activity.formatInterest("He.llo"), "Hello")
    }

}