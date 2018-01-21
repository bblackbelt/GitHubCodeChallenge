package com.blackbelt.githubcodechallenge.api

import android.support.test.InstrumentationRegistry
import com.blackbelt.github.api.IGitHubDataRepository
import com.blackbelt.github.api.model.*
import com.blackbelt.githubcodechallenge.android.JsonFileReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MockGitHubDataRepository @Inject constructor(gson: Gson) : IGitHubDataRepository {

    val mGson = gson

    override fun getForks(owner: String?, repo: String?): Observable<List<ForkResponseBody>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchRepositories(query: Map<String, String>): Observable<SearchResponseBody> {
        return Observable.just(query["q"]?.contains("ERROR") ?: false)
                .subscribeOn(Schedulers.computation())
                .flatMap { error ->
                    when (error) {
                        true -> Observable.error(Throwable())
                        false -> JsonFileReader.read(InstrumentationRegistry.getContext(),
                                "repositories.json", mGson, SearchResponseBody::class.java)
                    }
                }
    }

    override fun getRepositoryDetails(owner: String, repo: String): Observable<RepositoryDetailsResponseBody> {
        return Observable.just(repo)
                .subscribeOn(Schedulers.computation())
                .flatMap { theRepo ->
                    when (theRepo) {
                        "ERROR" -> Observable.error(Throwable())
                        "no_subscribers" -> JsonFileReader.read(InstrumentationRegistry.getContext(),
                                "repository_details_no_subscribers.json", mGson, RepositoryDetailsResponseBody::class.java)
                        else -> JsonFileReader.read(InstrumentationRegistry.getContext(),
                                "repository_details.json", mGson, RepositoryDetailsResponseBody::class.java)
                    }
                }
    }

    override fun getSubscribers(owner: String, repo: String, page: Int, pageSize: Int): Observable<List<OwnerResponseBody>> {
        val listType = object : TypeToken<List<OwnerResponseBody>>() {}
        return Observable.just(repo)
                .subscribeOn(Schedulers.computation())
                .flatMap { theRepo ->
                    when (theRepo) {
                        "ERROR" -> Observable.error(Throwable())
                        "no_subscribers" -> Observable.just(ArrayList())
                        else -> JsonFileReader.readList(InstrumentationRegistry.getContext(),
                                "subscribers_list.json", mGson, listType)
                    }
                }
    }

    override fun getRepositories(since: Int): Observable<List<RepositoryResponseBody>> {
        val listType = object : TypeToken<List<RepositoryResponseBody>>() {}
        return JsonFileReader.readList(InstrumentationRegistry.getContext(),
                "repository_details.json", mGson, listType)
    }

}