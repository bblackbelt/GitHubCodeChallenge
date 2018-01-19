package com.blackbelt.githubcodechallenge.repository

import com.blackbelt.githubcodechallenge.repository.model.Owner
import com.blackbelt.githubcodechallenge.repository.model.Repository
import com.blackbelt.githubcodechallenge.repository.model.RepositoryDetails
import io.reactivex.Observable

interface IRepositoryManager {

    fun searchRepositories(query: String, page: Int = 1, pageSize: Int = 25): Observable<List<Repository>>

    fun getRepositoryDetails(owner: String, repo: String): Observable<RepositoryDetails>

    fun getSubscribers(owner: String, repo: String, page: Int = 1, pageSize: Int = 25): Observable<List<Owner>>
}