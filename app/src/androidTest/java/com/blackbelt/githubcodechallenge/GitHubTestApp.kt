package com.blackbelt.githubcodechallenge

import com.blackbelt.githubcodechallenge.di.DaggerGitHubTestComponent
import com.facebook.drawee.backends.pipeline.Fresco

class GitHubTestApp : GitHubApp() {

    override fun onCreate() {
        super.onCreate()
        DaggerGitHubTestComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
        Fresco.initialize(this)
    }

}