package com.blackbelt.githubcodechallenge.di

import com.blackbelt.github.api.GitHubDataRepository
import com.blackbelt.github.api.IGitHubDataRepository
import dagger.Binds
import dagger.Module

@Module
abstract class BindsModule {

    @Binds
    abstract fun bindGitHubDataRepository(gitHubDataRepository: GitHubDataRepository): IGitHubDataRepository
}