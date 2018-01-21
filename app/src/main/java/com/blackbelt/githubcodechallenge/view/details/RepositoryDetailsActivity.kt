package com.blackbelt.githubcodechallenge.view.details

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.blackbelt.bindings.recyclerviewbindings.AndroidBindableRecyclerView
import com.blackbelt.githubcodechallenge.BR
import com.blackbelt.githubcodechallenge.R
import com.blackbelt.githubcodechallenge.view.details.viewmodel.RepositoryDetailsViewModel
import com.blackbelt.githubcodechallenge.view.misc.viewmodel.BaseInjectableBindingActivity
import com.blackbelt.githubcodechallenge.view.misc.viewmodel.ProgressLoader
import javax.inject.Inject

class RepositoryDetailsActivity : BaseInjectableBindingActivity() {

    @Inject
    lateinit var mFactory: RepositoryDetailsViewModel.Factory

    private val mRepositoryViewModel by lazy {
        ViewModelProviders.of(this, mFactory).get(RepositoryDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_details, BR.repositoryDetailsViewModel, mRepositoryViewModel)
        val recyclerView: AndroidBindableRecyclerView = findViewById(R.id.subscribers_rv)
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val dataSet = recyclerView.dataSet ?: return 1
                if (dataSet[position] is ProgressLoader) {
                    return 3
                }
                return 1
            }
        }
    }

    fun getViewModel(): RepositoryDetailsViewModel = mRepositoryViewModel
}