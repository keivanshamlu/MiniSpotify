package ui.activities



import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.minispotify.MockServerDispatcher
import com.example.minispotify.adapters.TracksAdapter
import com.example.minispotify.matchers.atPosition
import com.example.minispotify.ui.activities.MainActivity
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class ApplicationFlowTest {

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

    /**
     * senario : user opens app and click login from application and
     * authemticate , after opening search fragment , he/she searchs
     * something and after retrieving mock data from mock server , he/she
     * clicks on one item and after that checks details of that item
     */
    @Test
    fun appFlowTest() {

        mockWebServer.dispatcher = MockServerDispatcher(200).RequestDispatcher()

        withId(com.example.minispotify.R.id.toolbarTextView).matches(withText(containsString("Login")))

        val loginFromAppRelativeLayout =onView(withId(com.example.minispotify.R.id.loginFromApp))

        loginFromAppRelativeLayout.perform(click())

        withId(com.example.minispotify.R.id.toolbarTextView).matches(withText(containsString("Search")))

        onView(withId(com.example.minispotify.R.id.mainSearchText)).check(matches(isDisplayed()))

        onView(withId(com.example.minispotify.R.id.mainSearchText)).perform(typeText("k"))

        onView(withId(com.example.minispotify.R.id.tracksRecyclerView)).check(matches(atPosition(0, hasDescendant(withText("Roadhouse Blues")))))
        onView(withId(com.example.minispotify.R.id.tracksRecyclerView)).check(matches(atPosition(0, hasDescendant(withText("The Doors")))))
        onView(withId(com.example.minispotify.R.id.tracksRecyclerView)).check(matches(atPosition(0, hasDescendant(withText("Morrison Hotel")))))

        onView(withId(com.example.minispotify.R.id.tracksRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<TracksAdapter.TracksViewHolder>(0 , click()))


        onView(withId(com.example.minispotify.R.id.energy)).check(matches(withText(containsString("0.578"))))

        }
}
