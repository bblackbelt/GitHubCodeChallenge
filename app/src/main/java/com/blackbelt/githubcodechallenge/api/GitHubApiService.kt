package com.blackbelt.github.api

import com.blackbelt.github.api.model.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GitHubApiService {

    @GET("repositories")
    fun getRepositories(@Query("since") since: Int): Observable<List<RepositoryResponseBody>>

    @GET("repos/{owner}/{repo}/forks")
    fun getForks(@Path("owner") owner: String?, @Path("repo") repo: String?): Observable<List<ForkResponseBody>>

    @GET("search/repositories")
    fun searchRepositories(@QueryMap(encoded = true) query: Map<String, String>): Observable<SearchResponseBody>

    @GET("repos/{owner}/{repo}")
    fun getRepositoryDetails(@Path("owner") owner: String, @Path("repo") repo: String): Observable<RepositoryDetailsResponseBody>

    @GET("repos/{owner}/{repo}/subscribers")
    fun getSubscribers(@Path("owner") owner: String, @Path("repo") repo: String,
                       @Query("page") page: Int, @Query("per_page") pageSize: Int): Observable<List<OwnerResponseBody>>
}