package com.blackbelt.githubcodechallenge.view.repository.viewmodel

import android.content.res.Resources
import com.blackbelt.github.uti.RxClassTestRule
import com.blackbelt.githubcodechallenge.repository.IRepositoryManager
import com.blackbelt.githubcodechallenge.repository.model.Repository
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryViewModelTest {

    @Rule
    @JvmField
    public val schedulers = RxClassTestRule()

    @Mock
    private lateinit var mDataRepository: IRepositoryManager

    @Mock
    private lateinit var mResources: Resources

    private lateinit var mRepositoryViewModel: RepositoryViewModel

    @Before
    fun setup() {
        mRepositoryViewModel = Mockito.spy(RepositoryViewModel(mDataRepository, mResources))
        mRepositoryViewModel.onCreate()
    }

    @Test
    fun `test_search_call`() {
        val pageDescriptor = mRepositoryViewModel.getNextPage()
        Mockito.`when`(mDataRepository
                .searchRepositories("TEST",
                        pageDescriptor.getCurrentPage(),
                        pageDescriptor.getPageSize())).
                thenReturn(Observable.just(ArrayList()))

        mRepositoryViewModel.setSearchQueryStringChanged("TEST")
        Mockito.verify(mRepositoryViewModel).search("TEST")
    }

    @Test
    fun `test_search_empty_data_set`() {
        val pageDescriptor = mRepositoryViewModel.getNextPage()
        Mockito.`when`(mDataRepository
                .searchRepositories("TEST",
                        pageDescriptor.getCurrentPage(),
                        pageDescriptor.getPageSize())).
                thenReturn(Observable.just(ArrayList()))

        mRepositoryViewModel.setSearchQueryStringChanged("TEST")
        Assert.assertTrue(mRepositoryViewModel.getRepositories().isEmpty())
    }

    @Test
    fun `test_search_valid_data_set`() {
        val pageDescriptor = mRepositoryViewModel.getNextPage()
        val repository = Repository()
        Mockito.`when`(mDataRepository
                .searchRepositories("TEST",
                        pageDescriptor.getCurrentPage(),
                        pageDescriptor.getPageSize())).
                thenReturn(Observable.just(arrayListOf(repository)))

        mRepositoryViewModel.setSearchQueryStringChanged("TEST")
        Assert.assertTrue(!mRepositoryViewModel.getRepositories().isEmpty())
        Assert.assertTrue(mRepositoryViewModel.getRepositories().count() == 1)
        Assert.assertTrue(mRepositoryViewModel.getRepositories()[0] is RepositoryItemViewModel)
    }

    @Test
    fun `test_on_error`() {
        val pageDescriptor = mRepositoryViewModel.getNextPage()
        val throwable = Throwable()
        Mockito.`when`(mDataRepository
                .searchRepositories("TEST",
                        pageDescriptor.getCurrentPage(),
                        pageDescriptor.getPageSize())).
                thenReturn(Observable.error(throwable))

        mRepositoryViewModel.setSearchQueryStringChanged("TEST")
        Assert.assertTrue(mRepositoryViewModel.getRepositories().isEmpty())
        Mockito.verify(mRepositoryViewModel).handlerError(throwable)
    }

    @Test
    fun `test_clear_search`() {
        mRepositoryViewModel.clearSearch()
        Assert.assertFalse(mRepositoryViewModel.isClearSearchVisible())
        Assert.assertTrue(mRepositoryViewModel.getSearchQueryString().isNullOrEmpty())
    }
}