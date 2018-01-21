package com.blackbelt.githubcodechallenge.view.details.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.res.Resources
import android.databinding.Bindable
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.blackbelt.bindings.recyclerviewbindings.AndroidItemBinder
import com.blackbelt.bindings.recyclerviewbindings.PageDescriptor
import com.blackbelt.githubcodechallenge.BR
import com.blackbelt.githubcodechallenge.R
import com.blackbelt.githubcodechallenge.repository.IRepositoryManager
import com.blackbelt.githubcodechallenge.repository.model.Repository
import com.blackbelt.githubcodechallenge.repository.model.RepositoryDetails
import com.blackbelt.githubcodechallenge.view.misc.viewmodel.LoadingIndicatorViewModel
import com.blackbelt.githubcodechallenge.view.misc.viewmodel.ProgressLoader
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import javax.inject.Inject

open class RepositoryDetailsViewModel constructor(repositoryManager: IRepositoryManager,
                                                  repository: Repository, resources: Resources) : LoadingIndicatorViewModel() {

    private val mRepositoryManager = repositoryManager

    private val mResources = resources

    private val mRepository = repository

    private var mDetailsDisposable = Disposables.disposed()

    private var mRepositoryDetails: RepositoryDetails? = null

    private var mSubscribers: Disposable = Disposables.disposed()

    private val mItemDecoration by lazy {
        val margin: Int = mResources.getDimension(R.dimen.margin_4).toInt() / 2
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                val position = parent?.getChildAdapterPosition(view) ?: return
                outRect?.top = margin
                when {
                    position % 3 == 0 -> outRect?.left = margin
                    position % 3 == 2 -> outRect?.right = margin
                    else -> {
                        outRect?.left = margin / 2
                        outRect?.right = margin / 2
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        loadDetails()
    }

    private fun loadDetails() {
        if (mRepository.ownerName == null || mRepository.repositoryName == null) {
            return
        }
        mDetailsDisposable = mRepositoryManager
                .getRepositoryDetails(mRepository.ownerName, mRepository.repositoryName)
                .map { mRepositoryDetails = it }
                .subscribe({
                    notifyPropertyChanged(BR.avatarUrl)
                    notifyPropertyChanged(BR.name)
                    notifyPropertyChanged(BR.subscribers)
                    setNextPage(mPageDescriptor)
                }, Throwable::printStackTrace)
    }

    @Bindable
    fun getNextPage() = mPageDescriptor

    open fun setNextPage(pageDescriptor: PageDescriptor) {
        if (mRepositoryDetails == null) {
            return
        }
        handleLoading(true)
        val owner = mRepositoryDetails?.ownerName ?: return
        val repository = mRepositoryDetails?.repoName ?: return
        mSubscribers.dispose()
        mSubscribers = mRepositoryManager.getSubscribers(owner, repository,
                pageDescriptor.getCurrentPage(), pageDescriptor.getPageSize())
                .map { it.map { mItems.add(OwnerViewModel(it)) } }
                .subscribe({
                    handleLoading(false)
                    notifyPropertyChanged(BR.subscribers)
                    notifyPropertyChanged(BR.subscribersListVisible)
                }, this::handlerError)
    }

    override fun handlerError(throwable: Throwable) {
        super.handlerError(throwable)
        notifyPropertyChanged(BR.subscribersListVisible)
    }

    @Bindable
    fun getName(): String? {
        return mRepositoryDetails?.repoName
    }

    @Bindable
    fun getSubscribers(): String? {
        return mRepositoryDetails?.subscribersCount.toString()
    }

    @Bindable
    fun getAvatarUrl(): String? {
        return mRepositoryDetails?.avatarUrl
    }

    @Bindable
    fun getSubscribersList() = mItems

    @Bindable
    fun isSubscribersListVisible() = !mItems.isEmpty()

    @Bindable
    fun getItemDecoration(): RecyclerView.ItemDecoration = mItemDecoration

    override fun onCleared() {
        super.onCleared()
        mDetailsDisposable.dispose()
        mSubscribers.dispose()
    }

    @Bindable
    fun getTemplatesForObjects(): Map<Class<*>, AndroidItemBinder> = hashMapOf(
            ProgressLoader::class.java to AndroidItemBinder(R.layout.loading_progress, BR.progressLoader),
            OwnerViewModel::class.java to AndroidItemBinder(R.layout.subscriber_item, BR.subscriber))

    fun getRepositoryDetails() = mRepositoryDetails

    class Factory @Inject constructor(resources: Resources, repositoryManager: IRepositoryManager, repository: Repository) : ViewModelProvider.NewInstanceFactory() {

        private val mResource = resources

        private val mDataRepository = repositoryManager

        val mRepository = repository

        override fun <T : ViewModel?> create(modelClass: Class<T>) = RepositoryDetailsViewModel(mDataRepository, mRepository, mResource) as T
    }
}