package com.blackbelt.githubcodechallenge

import android.app.Activity
import android.app.Application
import com.blackbelt.githubcodechallenge.di.DaggerGitHubComponent
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

open class GitHubApp : Application(), HasActivityInjector {

    @Inject
    lateinit var mAndroidDispatchingInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerGitHubComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
        Fresco.initialize(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = mAndroidDispatchingInjector
}