package com.mmrnd.lunchbuddy

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Esteban Sosa
 */

@RunWith(AndroidJUnit4::class)
class MainAppInstrumentedTest {

    @get:Rule
    val mActivityTestRule = ActivityTestRule(MainAppActivity::class.java)

    @Test
    fun clickMyProfileTest() {
        onView(withId(R.id.main_menu_myprofile))
            .perform(click())
        onView(withId(R.id.my_profile_imageview))
            .check(matches(isDisplayed()))
    }
}