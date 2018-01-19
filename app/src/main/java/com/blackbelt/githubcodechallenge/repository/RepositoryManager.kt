package com.blackbelt.githubcodechallenge.repository

import com.blackbelt.github.api.IGitHubDataRepository
import com.blackbelt.githubcodechallenge.repository.model.Repository
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RepositoryManager @Inject constructor(dataRepository: IGitHubDataRepository) : IRepositoryManager {

    private val mDataRepository = dataRepository

    private var mSearchDisposable = Disposables.disposed()

    override fun searchRepositories(query: String, page: Int, pageSize: Int): Observable<List<Repository>> {

        val repositories: BehaviorSubject<List<Repository>> = BehaviorSubject.create()

        val list: MutableList<Repository> = mutableListOf()

        mSearchDisposable.dispose()
        mSearchDisposable = mDataRepository.searchRepositories(
                hashMapOf(
                        "page" to page.toString(),
                        "per_page" to pageSize.toString(),
                        "q" to "$query+fork:true"))
                .map { it -> it.items }
                .map {
                    it.map {
                        list.add(Repository(it.owner?.id,
                                it.id, it.owner?.login,
                                it.owner?.avatarUrl, it.name, it.description, it.forksCount))
                    }
                    list
                }
                .subscribe({
                    repositories.onNext(it)
                },

                        repositories::onError,
                        repositories::onComplete)
        return repositories.hide()
    }
}