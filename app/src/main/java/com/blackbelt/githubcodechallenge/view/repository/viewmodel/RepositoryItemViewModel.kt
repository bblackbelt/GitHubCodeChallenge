package com.blackbelt.githubcodechallenge.view.repository.viewmodel

import com.blackbelt.githubcodechallenge.repository.model.Repository

class RepositoryItemViewModel constructor(repository: Repository) {

    private val mRepository = repository

    fun getAvatarUrl(): String? = mRepository.ownerUrl

    fun getRepoName(): String? = mRepository.repositoryName

    fun getRepoDescription(): String? = mRepository.description

    fun getNoOfForks(): String? = mRepository.nuOfForks.toString()

    fun getRepository() = mRepository
}