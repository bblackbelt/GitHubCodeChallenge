package com.blackbelt.githubcodechallenge.di

import com.blackbelt.githubcodechallenge.view.repository.RepositoryActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindRepositoryActivity(): RepositoryActivity

}