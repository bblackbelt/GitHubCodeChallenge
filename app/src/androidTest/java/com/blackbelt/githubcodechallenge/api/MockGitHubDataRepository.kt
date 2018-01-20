package com.blackbelt.githubcodechallenge.api

import android.support.test.InstrumentationRegistry
import com.blackbelt.github.api.IGitHubDataRepository
import com.blackbelt.github.api.model.*
import com.blackbelt.githubcodechallenge.android.JsonFileReader
import com.google.gson.Gson
import io.reactivex.Observable
import javax.inject.Inject

class MockGitHubDataRepository @Inject constructor(gson: Gson) : IGitHubDataRepository {

    val mGson = gson

    override fun getForks(owner: String?, repo: String?): Observable<List<ForkResponseBody>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchRepositories(query: Map<String, String>): Observable<SearchResponseBody> {
        val error = query["q"]?.contains("ERROR") ?: false
        return when (error) {
            true -> Observable.error(Throwable())
            false -> JsonFileReader.read(InstrumentationRegistry.getContext(),
                    "repositories.json", mGson, SearchResponseBody::class.java)
        }
    }

    override fun getRepositoryDetails(owner: String, repo: String): Observable<RepositoryDetailsResponseBody> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSubscribers(owner: String, repo: String, page: Int, pageSize: Int): Observable<List<OwnerResponseBody>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRepositories(since: Int): Observable<List<RepositoryResponseBody>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}