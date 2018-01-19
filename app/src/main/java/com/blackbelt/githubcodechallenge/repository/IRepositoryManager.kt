package com.blackbelt.githubcodechallenge.repository

import com.blackbelt.githubcodechallenge.repository.model.Repository
import io.reactivex.Observable

interface IRepositoryManager {

    fun searchRepositories(query: String, page: Int = 1, pageSize: Int = 25): Observable<List<Repository>>
}