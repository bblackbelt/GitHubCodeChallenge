package com.blackbelt.githubcodechallenge.view.repository.viewmodel

import com.blackbelt.github.JsonFileReader
import com.blackbelt.githubcodechallenge.repository.model.Repository
import com.google.gson.Gson
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryItemViewModelTest {

    @Test
    fun `test_repository`() {
        val repository = getRepository()
        val repositoryViewModel = RepositoryItemViewModel(repository)
        Assert.assertEquals(repository, repositoryViewModel.getRepository())
    }

    @Test
    fun `test_repository_name`() {
        val repository = getRepository()
        val repositoryViewModel = RepositoryItemViewModel(repository)
        Assert.assertEquals(repository.repositoryName, repositoryViewModel.getRepoName())
    }

    @Test
    fun `test_repository_description`() {
        val repository = getRepository()
        val repositoryViewModel = RepositoryItemViewModel(repository)
        Assert.assertEquals(repository.description, repositoryViewModel.getRepoDescription())
    }

    @Test
    fun `test_repository_avatar_url`() {
        val repository = getRepository()
        val repositoryViewModel = RepositoryItemViewModel(repository)
        Assert.assertEquals(repository.ownerUrl, repositoryViewModel.getAvatarUrl())
    }

    @Test
    fun `test_repository_no_of_forks`() {
        val repository = getRepository()
        val repositoryViewModel = RepositoryItemViewModel(repository)
        Assert.assertEquals(repository.nuOfForks.toString(), repositoryViewModel.getNoOfForks())
    }

    private fun getRepository(): Repository {
        return JsonFileReader.read(javaClass.classLoader.getResourceAsStream("repository.json"),
                Gson(), Repository::class.java).blockingFirst()
    }
}