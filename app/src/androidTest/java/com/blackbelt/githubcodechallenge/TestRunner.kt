package com.blackbelt.githubcodechallenge

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins

class TestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, GitHubTestApp::class.java.name, context)
    }

    override fun onStart() {
        super.onStart()
        RxJavaPlugins.setInitComputationSchedulerHandler(Rx2Idler.create(""))
        RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create(""))
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(Rx2Idler.create(""))
    }

}