package com.blackbelt.githubcodechallenge.view.repository.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.res.Resources
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.blackbelt.bindings.notifications.ClickItemWrapper
import com.blackbelt.bindings.notifications.MessageWrapper
import com.blackbelt.bindings.recyclerviewbindings.AndroidItemBinder
import com.blackbelt.bindings.recyclerviewbindings.ItemClickListener
import com.blackbelt.bindings.recyclerviewbindings.PageDescriptor
import com.blackbelt.bindings.viewmodel.BaseViewModel
import com.blackbelt.githubcodechallenge.BR
import com.blackbelt.githubcodechallenge.R
import com.blackbelt.githubcodechallenge.repository.IRepositoryManager
import com.blackbelt.githubcodechallenge.view.misc.viewmodel.ProgressLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


open class RepositoryViewModel constructor(repositoryManager: IRepositoryManager, resources: Resources) : BaseViewModel() {

    private val mResources = resources

    private val mTemplates: Map<Class<*>, AndroidItemBinder> =
            hashMapOf(ProgressLoader::class.java to AndroidItemBinder(R.layout.loading_progress, BR.progressLoader),
                    RepositoryItemViewModel::class.java to AndroidItemBinder(R.layout.repository_item, BR.repositoryItemViewModel))

    private var mFirstLoading: Boolean = false

    protected var mPageDescriptor = PageDescriptor.PageDescriptorBuilder
            .setPageSize(24)
            .setStartPage(1)
            .setThreshold(5)
            .build()

    protected val mProgressLoader: ProgressLoader = ProgressLoader()

    private val mRepositoryManager = repositoryManager

    private var mRepositoriesDisposable = Disposables.disposed()

    private var mSearchNotifierDisposable = Disposables.disposed()

    private val mSearchStringPublishSubject: PublishSubject<String> = PublishSubject.create()

    private var isSearchFieldEmpty: Boolean = true

    private var mCurrentSearchString = ""

    private val mItemDecoration by lazy {
        val margin: Int = mResources.getDimension(R.dimen.margin_4).toInt()
        val lateralMargin: Int = mResources.getDimension(R.dimen.margin_16).toInt()
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.bottom = margin
                outRect?.top = margin
                outRect?.left = lateralMargin
                outRect?.right = lateralMargin
            }
        }
    }

    @Bindable
    fun getTemplatesForObjects(): Map<Class<*>, AndroidItemBinder> = mTemplates

    @Bindable
    fun getRepositories(): List<Any> = mItems

    @Bindable
    fun getItemDecoration(): RecyclerView.ItemDecoration = mItemDecoration

    @Bindable
    fun getNextPage() = mPageDescriptor

    fun setNextPage(pageDescriptor: PageDescriptor) {
        mRepositoriesDisposable.dispose()
        handleLoading(true)
        mRepositoriesDisposable =
                mRepositoryManager.searchRepositories(mCurrentSearchString, pageDescriptor.getCurrentPage(), pageDescriptor.getPageSize())
                        .map {
                            it.map { mItems.add(RepositoryItemViewModel(it)) }
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            handleLoading(false)
                            notifyPropertyChanged(BR.repositories)
                        }, this::handlerError)
    }

    override fun onCreate() {
        super.onCreate()
        subscribeForSearchQueryChanges()
    }

    private fun subscribeForSearchQueryChanges() {
        mSearchNotifierDisposable =
                mSearchStringPublishSubject
                        .map { searchString ->
                            isSearchFieldEmpty = searchString.isEmpty()
                            notifyPropertyChanged(BR.clearSearchVisible)
                            searchString
                        }
                        .debounce(1, TimeUnit.SECONDS)
                        .filter { s -> !s.isEmpty() }
                        .subscribe(this::search, Throwable::printStackTrace)
    }

    open fun search(repo: String) {
        mItems.clear()
        notifyPropertyChanged(BR.repositories)
        mCurrentSearchString = repo
        mPageDescriptor.setCurrentPage(1)
        setNextPage(mPageDescriptor)
    }

    fun setSearchQueryStringChanged(text: String) {
        mSearchStringPublishSubject.onNext(text)
    }

    @Bindable
    fun isClearSearchVisible(): Boolean = !isSearchFieldEmpty

    fun clearSearch() {
        mCurrentSearchString = ""
        isSearchFieldEmpty = true
        notifyPropertyChanged(BR.searchQueryString)
    }

    @Bindable
    fun getSearchQueryString(): String = mCurrentSearchString

    fun getItemClickListener(): ItemClickListener {
        return object : ItemClickListener {
            override fun onItemClicked(view: View, item: Any) {
                val repositoryViewModel = item as? RepositoryItemViewModel ?: return
                mItemClickNotifier.value = ClickItemWrapper.withAdditionalData(0, repositoryViewModel.getRepository())
            }
        }
    }

    fun getItemClickNotifier() = mItemClickNotifier

    override fun onCleared() {
        super.onCleared()
        mRepositoriesDisposable.dispose()
        mSearchNotifierDisposable.dispose()
    }

    private val mItems: MutableList<Any> = mutableListOf()

    private fun handleLoading(loading: Boolean) {
        if (mPageDescriptor.getCurrentPage() == 1) {
            setFistLoading(loading)
        } else {
            setLoading(loading)
        }
    }

    private fun setFistLoading(loading: Boolean) {
        mFirstLoading = loading
        notifyPropertyChanged(BR.firstLoading)
    }

    private fun setLoading(loading: Boolean) {
        if (loading && !mItems.contains(mProgressLoader)) {
            mItems.add(mProgressLoader)
        } else if (!loading) {
            mItems.remove(mProgressLoader)
        }
        notifyPropertyChanged(BR.repositories)
    }

    override fun handlerError(throwable: Throwable) {
        super.handlerError(throwable)
        mMessageNotifier.value = MessageWrapper.Companion.withSnackBar(throwable.message ?: return)
        handleLoading(false)
    }

    @Bindable
    fun isFirstLoading(): Boolean = mFirstLoading

    class Factory @Inject constructor(resources: Resources, repositoryManager: IRepositoryManager) : ViewModelProvider.NewInstanceFactory() {

        private val mResource = resources

        private val mDataRepository = repositoryManager

        override fun <T : ViewModel?> create(modelClass: Class<T>) = RepositoryViewModel(mDataRepository, mResource) as T
    }
}