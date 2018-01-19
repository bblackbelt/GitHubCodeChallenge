package com.blackbelt.githubcodechallenge.view.details.di

import com.blackbelt.githubcodechallenge.repository.model.Repository
import com.blackbelt.githubcodechallenge.view.details.RepositoryDetailsActivity
import dagger.Module
import dagger.Provides


const val REPOSITORY_KEY = "REPOSITORY_KEY"

@Module
class RepositoryDetailsModule {

    @Provides
    fun provideRepository(repositoryDetailsActivity: RepositoryDetailsActivity) =
            repositoryDetailsActivity.intent.getParcelableExtra<Repository>(REPOSITORY_KEY)
}