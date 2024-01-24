package com.example.mars.ui.main

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mars.R
import com.example.mars.entity.Rover
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThirdFragmentTest {
    @Test
    fun checkWeatherInfoDisplayed() {
        val rover = Rover(
            id = 1,
            landing_date = "",
            launch_date = "",
            name = "Rover",
            status = ""
        )
        val bundle = Bundle()
        bundle.putString("rover", rover.name)
        val scenario = launchFragmentInContainer<ThirdFragment>(
            fragmentArgs = bundle
        )

        Espresso.onView(withId(R.id.rover))
            .check(matches(ViewMatchers.withText("Rover: ${rover.name}")))
    }
}