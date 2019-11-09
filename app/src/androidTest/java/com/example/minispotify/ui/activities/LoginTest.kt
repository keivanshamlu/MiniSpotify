package com.example.minispotify.ui.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.minispotify.MockServerDispatcher
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginTest {

    @Before
    fun init(){

        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(mActivityTestRule.activity.idlingResource)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(mActivityTestRule.activity.idlingResource)
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    lateinit var mockWebServer: MockWebServer

    @Test
    fun loginTest() {

        mockWebServer.dispatcher = MockServerDispatcher(200).RequestDispatcher()

        withId(com.example.minispotify.R.id.toolbarTextView).matches(withText(containsString("Login")))

        onView(withId(com.example.minispotify.R.id.loginFromApp)).perform(click())

        withId(com.example.minispotify.R.id.toolbarTextView).matches(withText(containsString("Search")))
    }


}
