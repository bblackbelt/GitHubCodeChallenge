package com.blackbelt.githubcodechallenge.view.misc.viewmodel

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import com.blackbelt.bindings.recyclerviewbindings.PageDescriptor
import com.blackbelt.bindings.viewmodel.BaseViewModel
import com.blackbelt.githubcodechallenge.BR

open class LoadingIndicatorViewModel : BaseViewModel() {

    private var mFirstLoading: Boolean = false

    protected var mPageDescriptor = PageDescriptor.PageDescriptorBuilder
            .setPageSize(24)
            .setStartPage(1)
            .setThreshold(5)
            .build()

    protected val mProgressLoader: ProgressLoader = ProgressLoader()

    protected val mItems = ObservableArrayList<Any>()

    protected fun handleLoading(loading: Boolean) {
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

    @Bindable
    fun isFirstLoading(): Boolean = mFirstLoading
}