package com.blackbelt.githubcodechallenge.view.repository

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.blackbelt.githubcodechallenge.R
import com.blackbelt.githubcodechallenge.view.details.RepositoryDetailsActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RepositoryActivityTest {

    @JvmField
    @Rule
    var mRepositoryActivity: ActivityTestRule<RepositoryActivity> =
            ActivityTestRule(RepositoryActivity::class.java, false, false)

    @Before
    fun setUp() {
        mRepositoryActivity.launchActivity(Intent())
        Assert.assertTrue(mRepositoryActivity.activity != null)
    }

    @Test
    fun test_dataset_valid() {
        Espresso.onView(withId(R.id.search_input_text))
                .perform(ViewActions.typeText("TEST"))
        ViewActions.closeSoftKeyboard()
        val recyclerView = mRepositoryActivity.activity.findViewById<RecyclerView>(R.id.repository_rv)
        Assert.assertTrue(recyclerView != null)
        Assert.assertTrue(recyclerView.adapter != null)

        Thread.sleep(3000)

        Espresso.onView(withId(R.id.repository_rv))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        val count: Int = recyclerView.adapter.itemCount
        Assert.assertTrue(count == 24)
    }

    @Test
    fun test_dataset_next_activity() {
        Espresso.onView(withId(R.id.search_input_text))
                .perform(ViewActions.typeText("TEST"))
        ViewActions.closeSoftKeyboard()
        val recyclerView = mRepositoryActivity.activity.findViewById<RecyclerView>(R.id.repository_rv)
        Assert.assertTrue(recyclerView != null)
        Assert.assertTrue(recyclerView.adapter != null)

        Thread.sleep(3000)

        Espresso.onView(withId(R.id.repository_rv))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        val count: Int = recyclerView.adapter.itemCount
        Assert.assertTrue(count == 24)

        val activityMonitor = InstrumentationRegistry.getInstrumentation()
                .addMonitor(RepositoryDetailsActivity::class.java.name, null, true)

        Espresso.onView(ViewMatchers.withId(R.id.repository_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))

        Thread.sleep(1000)

        Assert.assertTrue(activityMonitor.hits == 1)

        InstrumentationRegistry.getInstrumentation().removeMonitor(activityMonitor)
    }

    @Test
    fun test_on_error() {
        Espresso.onView(withId(R.id.search_input_text))
                .perform(ViewActions.typeText("ERROR"))
        ViewActions.closeSoftKeyboard()
        val recyclerView = mRepositoryActivity.activity.findViewById<RecyclerView>(R.id.repository_rv)
        Assert.assertTrue(recyclerView != null)
        Assert.assertTrue(recyclerView.adapter != null)

        Thread.sleep(3000)

        Espresso.onView(withId(R.id.repository_rv))
                .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        val count: Int = recyclerView.adapter.itemCount
        Assert.assertTrue(count == 0)
    }
}