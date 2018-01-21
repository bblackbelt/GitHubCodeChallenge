package com.blackbelt.githubcodechallenge.view.details

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.blackbelt.github.api.model.SearchItemResponsBody
import com.blackbelt.githubcodechallenge.R
import com.blackbelt.githubcodechallenge.android.JsonFileReader
import com.blackbelt.githubcodechallenge.repository.model.Repository
import com.blackbelt.githubcodechallenge.view.details.di.REPOSITORY_KEY
import com.google.gson.Gson
import org.hamcrest.CoreMatchers.not
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RepositoryDetailsActivityTest {

    @JvmField
    @Rule
    var mRepositoryDetailsActivity: ActivityTestRule<RepositoryDetailsActivity> =
            ActivityTestRule(RepositoryDetailsActivity::class.java, false, false)

    lateinit var mActivity: RepositoryDetailsActivity

    private val repository: Repository by lazy {
        val responseBody = JsonFileReader.read(InstrumentationRegistry.getContext(), "repository.json",
                Gson(), SearchItemResponsBody::class.java).blockingFirst()
        Repository(responseBody.owner?.id,
                responseBody.id, responseBody.owner?.login,
                responseBody.owner?.avatarUrl, responseBody.name, responseBody.description, responseBody.forksCount)
    }


    @Test
    fun test_activity_details_completely_displayed() {

        val intent = Intent()
        intent.putExtra(REPOSITORY_KEY, repository)
        mRepositoryDetailsActivity.launchActivity(intent)
        mActivity = mRepositoryDetailsActivity.activity

        val viewModel = mActivity.getViewModel()

        val repoDetails = viewModel.getRepositoryDetails()

        Assert.assertTrue(repoDetails != null)
        Assert.assertTrue(viewModel.getAvatarUrl()?.equals(repoDetails?.avatarUrl) ?: false)
        Assert.assertTrue(viewModel.getName()?.equals(repoDetails?.repoName) ?: false)

        Espresso.onView(ViewMatchers.withText(repoDetails?.repoName))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val subscribers = InstrumentationRegistry.getTargetContext().getString(R.string.subscribers,
                repoDetails?.subscribersCount.toString())

        Espresso.onView(ViewMatchers.withText(subscribers))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(1000)

        Assert.assertFalse(viewModel.getSubscribersList().isEmpty())

        Espresso.onView(ViewMatchers.withId(R.id.progress_bar))
                .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.subscribers_rv))
                .check(ViewAssertions.matches((ViewMatchers.isDisplayed())))
    }

    @Test
    fun test_activity_details_no_subscribers() {

        val intent = Intent()
        val repository = Repository(0, 0, "no_subscribers", "no_subscribers", "no_subscribers")
        intent.putExtra(REPOSITORY_KEY, repository)
        mRepositoryDetailsActivity.launchActivity(intent)
        mActivity = mRepositoryDetailsActivity.activity

        val viewModel = mActivity.getViewModel()

        val repoDetails = viewModel.getRepositoryDetails()

        Assert.assertTrue(repoDetails != null)
        Assert.assertTrue(viewModel.getAvatarUrl()?.equals(repoDetails?.avatarUrl) ?: false)
        Assert.assertTrue(viewModel.getName()?.equals(repoDetails?.repoName) ?: false)

        Espresso.onView(ViewMatchers.withText(repoDetails?.repoName))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val subscribers = InstrumentationRegistry.getTargetContext().getString(R.string.subscribers,
                repoDetails?.subscribersCount.toString())

        Espresso.onView(ViewMatchers.withText(subscribers))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(1000)

        Assert.assertTrue(viewModel.getSubscribersList().isEmpty())

        Espresso.onView(ViewMatchers.withId(R.id.progress_bar))
                .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.subscribers_rv))
                .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun test_activity_details_error() {
        val intent = Intent()
        val repository = Repository(0, 0, "ERROR", "ERROR", "ERROR")
        intent.putExtra(REPOSITORY_KEY, repository)
        mRepositoryDetailsActivity.launchActivity(intent)
        mActivity = mRepositoryDetailsActivity.activity

        Espresso.onView(ViewMatchers.withId(R.id.progress_bar))
                .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.subscribers_rv))
                .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    }
}