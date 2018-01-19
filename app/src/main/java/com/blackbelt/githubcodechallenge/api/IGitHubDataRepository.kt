package com.blackbelt.github.api

import com.blackbelt.github.api.model.*
import io.reactivex.Observable


interface IGitHubDataRepository {
    fun getRepositories(since: Int = 0): Observable<List<RepositoryResponseBody>>
    fun getForks(owner: String?, repo: String?): Observable<List<ForkResponseBody>>
    fun searchRepositories(query: Map<String, String>): Observable<SearchResponseBody>
    fun getRepositoryDetails(owner: String, repo: String): Observable<RepositoryDetailsResponseBody>
    fun getSubscribers(owner: String, repo: String, page: Int = 1, pageSize: Int = 25): Observable<List<OwnerResponseBody>>
}