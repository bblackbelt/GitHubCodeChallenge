package com.blackbelt.githubcodechallenge.view.details.viewmodel

import android.content.res.Resources
import com.blackbelt.github.uti.RxClassTestRule
import com.blackbelt.githubcodechallenge.JsonFileReader
import com.blackbelt.githubcodechallenge.repository.IRepositoryManager
import com.blackbelt.githubcodechallenge.repository.model.Owner
import com.blackbelt.githubcodechallenge.repository.model.Repository
import com.blackbelt.githubcodechallenge.repository.model.RepositoryDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class RepositoryDetailsViewModelTest {

    @Rule
    @JvmField
    val schedulers = RxClassTestRule()

    @Mock
    private lateinit var mDataRepository: IRepositoryManager

    @Mock
    private lateinit var mResources: Resources

    private val mRepositoryDetailsViewModel: RepositoryDetailsViewModel by lazy {
        Mockito.spy(RepositoryDetailsViewModel(mDataRepository, mRepository, mResources))
    }

    private val mRepository: Repository by lazy {
        getRepository()
    }

    @Test
    fun `test_null_repository`() {
        val repositoryDetails = Mockito.spy(RepositoryDetailsViewModel(mDataRepository, Repository(), mResources));
        repositoryDetails.onCreate()
        Mockito.verify(repositoryDetails, Mockito.never())
                .setNextPage(repositoryDetails.getNextPage())
    }

    @Test
    fun `test_repository_no_subscribers`() {
        val pageDescriptor = mRepositoryDetailsViewModel.getNextPage()
        val name = mRepository.ownerName ?: return
        val repo = mRepository.repositoryName ?: return
        val repoDetails = getRepositoryDetails()

        Mockito.`when`(mDataRepository.getRepositoryDetails(name, repo))
                .thenReturn(Observable.just(repoDetails))

        Mockito.`when`(mDataRepository.getSubscribers(name, repo, pageDescriptor.getCurrentPage(), pageDescriptor.getPageSize()))
                .thenReturn(Observable.just(ArrayList()))

        mRepositoryDetailsViewModel.onCreate()
        Assert.assertEquals(mRepositoryDetailsViewModel.getAvatarUrl(), repoDetails.avatarUrl)
        Assert.assertEquals(mRepositoryDetailsViewModel.getName(), repoDetails.repoName)
        Assert.assertEquals(mRepositoryDetailsViewModel.getSubscribers(), repoDetails.subscribersCount.toString())
        Assert.assertTrue(mRepositoryDetailsViewModel.getSubscribersList().isNotEmpty())
        Assert.assertTrue(mRepositoryDetailsViewModel.getSubscribersList()[0] is OwnerViewModel)
    }

    @Test
    fun `test_repository_subscribers`() {
        val pageDescriptor = mRepositoryDetailsViewModel.getNextPage()
        val name = mRepository.ownerName ?: return
        val repo = mRepository.repositoryName ?: return
        val repoDetails = getRepositoryDetails()

        Mockito.`when`(mDataRepository.getRepositoryDetails(name, repo))
                .thenReturn(Observable.just(repoDetails))

        Mockito.`when`(mDataRepository.getSubscribers(name, repo, pageDescriptor.getCurrentPage(), pageDescriptor.getPageSize()))
                .thenReturn(Observable.just(getSubscribersList()))

        mRepositoryDetailsViewModel.onCreate()
        Assert.assertEquals(mRepositoryDetailsViewModel.getAvatarUrl(), repoDetails.avatarUrl)
        Assert.assertEquals(mRepositoryDetailsViewModel.getName(), repoDetails.repoName)
        Assert.assertEquals(mRepositoryDetailsViewModel.getSubscribers(), repoDetails.subscribersCount.toString())
        Assert.assertTrue(mRepositoryDetailsViewModel.getSubscribersList().isEmpty())
    }

    @Test
    fun `test_repository_subscribers_error`() {
        val pageDescriptor = mRepositoryDetailsViewModel.getNextPage()
        val name = mRepository.ownerName ?: return
        val repo = mRepository.repositoryName ?: return
        val repoDetails = getRepositoryDetails()

        Mockito.`when`(mDataRepository.getRepositoryDetails(name, repo))
                .thenReturn(Observable.just(repoDetails))

        val throwable = Throwable()
        Mockito.`when`(mDataRepository.getSubscribers(name, repo, pageDescriptor.getCurrentPage(), pageDescriptor.getPageSize()))
                .thenReturn(Observable.error(throwable))

        mRepositoryDetailsViewModel.onCreate()

        Assert.assertEquals(mRepositoryDetailsViewModel.getAvatarUrl(), repoDetails.avatarUrl)
        Assert.assertEquals(mRepositoryDetailsViewModel.getName(), repoDetails.repoName)
        Assert.assertEquals(mRepositoryDetailsViewModel.getSubscribers(), repoDetails.subscribersCount.toString())
        Assert.assertTrue(mRepositoryDetailsViewModel.getSubscribersList().isEmpty())
        Mockito.verify(mRepositoryDetailsViewModel).handlerError(throwable)
    }

    private fun getSubscribersList(): List<Owner> {
        return JsonFileReader.readList<Owner>(javaClass.classLoader.getResourceAsStream("subscribers_list.json"),
                Gson(), object : TypeToken<List<Owner>>() {}.type).blockingFirst()
    }

    private fun getRepository(): Repository {
        return JsonFileReader.read(javaClass.classLoader.getResourceAsStream("repository.json"),
                Gson(), Repository::class.java).blockingFirst()
    }

    private fun getRepositoryDetails(): RepositoryDetails {
        return JsonFileReader.read(javaClass.classLoader.getResourceAsStream("repository_details.json"),
                Gson(), RepositoryDetails::class.java).blockingFirst()
    }
}