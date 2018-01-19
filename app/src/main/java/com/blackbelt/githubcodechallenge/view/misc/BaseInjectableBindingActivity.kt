package com.blackbelt.githubcodechallenge.view.misc.viewmodel

import android.os.Bundle
import com.blackbelt.bindings.activity.BaseBindingActivity
import dagger.android.AndroidInjection

abstract class BaseInjectableBindingActivity : BaseBindingActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}