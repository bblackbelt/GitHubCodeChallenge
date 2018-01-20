package com.blackbelt.githubcodechallenge.di

import com.blackbelt.github.api.IGitHubDataRepository
import com.blackbelt.githubcodechallenge.api.MockGitHubDataRepository
import com.blackbelt.githubcodechallenge.repository.IRepositoryManager
import com.blackbelt.githubcodechallenge.repository.RepositoryManager
import dagger.Binds
import dagger.Module

@Module
abstract class BindsModuleTest {

    @Binds
    abstract fun bindGitHubDataRepository(gitHubDataRepository: MockGitHubDataRepository): IGitHubDataRepository

    @Binds
    abstract fun bindRepositoryManager(repositoryManager: RepositoryManager): IRepositoryManager
}