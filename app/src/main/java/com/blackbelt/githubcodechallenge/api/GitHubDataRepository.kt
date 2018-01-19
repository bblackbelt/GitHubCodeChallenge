package com.blackbelt.github.api

import com.blackbelt.github.api.model.*
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
class GitHubDataRepository constructor(gitHubApiService: GitHubApiService) : IGitHubDataRepository {
    private val mGitHubApiService = gitHubApiService

    override fun getRepositories(since: Int): Observable<List<RepositoryResponseBody>> = mGitHubApiService.getRepositories(since)

    override fun getForks(owner: String?, repo: String?): Observable<List<ForkResponseBody>> = mGitHubApiService.getForks(owner, repo)

    override fun searchRepositories(query: Map<String, String>): Observable<SearchResponseBody> = mGitHubApiService.searchRepositories(query)

    override fun getRepositoryDetails(owner: String, repo: String): Observable<RepositoryDetailsResponseBody> = mGitHubApiService.getRepositoryDetails(owner, repo)

    override fun getSubscribers(owner: String, repo: String, page: Int, pageSize: Int):
            Observable<List<OwnerResponseBody>> = mGitHubApiService.getSubscribers(owner, repo, page, pageSize)
}