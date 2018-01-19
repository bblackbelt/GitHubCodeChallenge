package com.blackbelt.githubcodechallenge.view.details.viewmodel

import com.blackbelt.githubcodechallenge.repository.model.Owner

open class OwnerViewModel constructor(val owner: Owner) {

    fun getName() = owner.name

    fun getAvatarUrl() = owner.url
}