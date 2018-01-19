package com.blackbelt.githubcodechallenge.di

import com.blackbelt.githubcodechallenge.view.details.RepositoryDetailsActivity
import com.blackbelt.githubcodechallenge.view.details.di.RepositoryDetailsModule
import com.blackbelt.githubcodechallenge.view.repository.RepositoryActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindRepositoryActivity(): RepositoryActivity

    @ContributesAndroidInjector(modules = arrayOf(RepositoryDetailsModule::class))
    abstract fun bindRepositoryDetailsActivity(): RepositoryDetailsActivity
}