package com.cory.streamline.gallery


import com.cory.streamline.model.exception.CategoryNotFoundException
import com.cory.streamline.model.web.Fetchable
import com.cory.streamline.model.web.WebSource
import com.cory.streamline.util.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class GalleryPresenter(
    private var galleryView: IGalleryView?,
    sourceString: String,
    private val category: String
) : IGalleryPresenter {
    private var disposable: Disposable? = null
    private val source: WebSource<*, *> = createWebSourceBy(sourceString)

    override fun fetchingImages(isInitial: Boolean) {
        when (category) {
            CATEGORY_LATEST -> getLatestResults(isInitial)
            CATEGORY_POPULAR -> getPopularResults(isInitial)
            CATEGORY_FAVORITE -> getFavoriteRecords(isInitial)
            CATEGORY_HISTORY -> getHistoryRecords(isInitial)
            else -> throw CategoryNotFoundException(category)
        }
    }

    override fun onDestroy() {
        unsubscribeLast()
        galleryView = null
    }

    private fun getLatestResults(isInitial: Boolean) {
        val observables =
            source.getLatestImageUrls() ?: throw CategoryNotFoundException("latest")
        executeUrls(observables, isInitial)
    }

    private fun getPopularResults(isInitial: Boolean) {
        val observables =
            source.getPopularImageUrls() ?: throw CategoryNotFoundException("popular")
        executeUrls(observables, isInitial)
    }

    private fun getFavoriteRecords(isInitial: Boolean) {
        val loggedUser = user
        if (loggedUser != null) {
            val observables =
                source.getFavoriteImageUrls(loggedUser.token) ?: throw CategoryNotFoundException("favorite")
            executeUrls(observables, isInitial)
        }
    }

    private fun getHistoryRecords(isInitial: Boolean) {
        val loggedUser = user
        if (loggedUser != null) {
            val observables =
                source.getHistoryImageUrls(loggedUser.token) ?: throw CategoryNotFoundException("history")
            executeUrls(observables, isInitial)
        }
    }

    private fun executeUrls(observables: Observable<out Fetchable>, isInitial: Boolean) {
        observables.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Fetchable> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: Fetchable) {
                    val sources = t.getImageSources()
//                    sources.forEach { log("onNext,element:\n$it") }
                    if (isInitial) {
                        galleryView?.onImagesFetched(sources)
                    } else {
                        galleryView?.onMoreImagesFetched(sources)
                    }
                }

                override fun onError(e: Throwable) {
                    log("onError:$e")
                    toast("onError:$e")
                }

            })
    }

    private fun unsubscribeLast() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }
}