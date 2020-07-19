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
    private val sourceString: String,
    private val category: String
) : IGalleryPresenter {
    private var disposable: Disposable? = null
    private val source: WebSource<*> = createWebSourceBy(sourceString)

    override fun getLatestResults(isInitial: Boolean) {
        val observables =
            source.getLatestImageUrls() ?: throw CategoryNotFoundException("latest")
        executeUrls(observables, isInitial)
    }

    override fun getPopularResults(isInitial: Boolean) {
        val observables =
            source.getPopularImageUrls() ?: throw CategoryNotFoundException("popular")
        executeUrls(observables, isInitial)
    }

    override fun onDestroy() {
        unsubscribeLast()
        galleryView = null
    }

    private fun executeUrls(observables: Observable<out Fetchable>, isInitial: Boolean) {
        observables.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Fetchable> {
                override fun onComplete() {
                    log("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    log("onSubscribe")
                }

                override fun onNext(t: Fetchable) {
                    val sources = t.getImageSources()
                    sources.forEach { log("onNext,element:\n$it") }
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