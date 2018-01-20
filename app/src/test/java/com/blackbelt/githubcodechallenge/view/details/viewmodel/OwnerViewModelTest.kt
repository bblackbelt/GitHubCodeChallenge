package com.blackbelt.githubcodechallenge.view.details.viewmodel

import com.blackbelt.githubcodechallenge.JsonFileReader
import com.blackbelt.githubcodechallenge.repository.model.Owner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OwnerViewModelTest {

    @Test
    fun `test_owner`() {
        val owner = getOwner()
        val ownerViewModel = OwnerViewModel(owner)
        Assert.assertEquals(ownerViewModel.owner, owner)
        Assert.assertEquals(ownerViewModel.getName(), owner.name)
        Assert.assertEquals(ownerViewModel.getAvatarUrl(), owner.url)
    }

    private fun getOwner(): Owner {
        val ownersList = JsonFileReader.readList<Owner>(javaClass.classLoader.getResourceAsStream("subscribers_list.json"),
                Gson(), object : TypeToken<List<Owner>>() {}.type).blockingFirst().toList()

        return ownersList[0]
    }

}