package com.blackbelt.githubcodechallenge.di

import com.blackbelt.github.api.GitHubDataRepository
import com.blackbelt.github.api.IGitHubDataRepository
import com.blackbelt.githubcodechallenge.repository.IRepositoryManager
import com.blackbelt.githubcodechallenge.repository.RepositoryManager
import dagger.Binds
import dagger.Module

@Module
abstract class BindsModule {

    @Binds
    abstract fun bindGitHubDataRepository(gitHubDataRepository: GitHubDataRepository): IGitHubDataRepository

    @Binds
    abstract fun bindRepositoryManager(repositoryManager: RepositoryManager): IRepositoryManager
}