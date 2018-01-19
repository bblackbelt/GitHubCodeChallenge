package com.blackbelt.githubcodechallenge.repository

import com.blackbelt.github.api.IGitHubDataRepository
import com.blackbelt.githubcodechallenge.repository.model.Owner
import com.blackbelt.githubcodechallenge.repository.model.Repository
import com.blackbelt.githubcodechallenge.repository.model.RepositoryDetails
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RepositoryManager @Inject constructor(dataRepository: IGitHubDataRepository) : IRepositoryManager {

    private val mDataRepository = dataRepository

    private var mSearchDisposable = Disposables.disposed()

    private var mSubscribersDisposable = Disposables.disposed()

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

    override fun getRepositoryDetails(owner: String, repo: String): Observable<RepositoryDetails> {
        return mDataRepository.getRepositoryDetails(owner, repo)
                .map { repoDetails ->
                    RepositoryDetails(
                            repoDetails.name,
                            repoDetails.owner?.avatarUrl,
                            repoDetails.owner?.login,
                            repoDetails.subscribersCount,
                            repoDetails.subscribersUrl)
                }
    }

    override fun getSubscribers(owner: String, repo: String, page: Int, pageSize: Int): Observable<List<Owner>> {
        val mOwner: BehaviorSubject<List<Owner>> = BehaviorSubject.create()
        val list: MutableList<Owner> = mutableListOf()
        mSubscribersDisposable.dispose()
        mSubscribersDisposable = mDataRepository.getSubscribers(owner, repo, page, pageSize)
                .map {
                    it.map { list.add(Owner(it.login, it.avatarUrl)) }
                    return@map list
                }
                .subscribe(mOwner::onNext, mOwner::onError, mOwner::onComplete)

        return mOwner.hide()
    }

}