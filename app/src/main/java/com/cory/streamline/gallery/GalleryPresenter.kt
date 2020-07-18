package com.cory.streamline.gallery


import com.cory.streamline.model.exception.CategoryNotFoundException
import com.cory.streamline.model.web.Fetchable
import com.cory.streamline.model.web.WebSource
import com.cory.streamline.util.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class GalleryPresenter(
    private var galleryView: IGalleryView?,
    var sourceString: String,
    var category: String
) : IGalleryPresenter {
    private var disposable: Disposable? = null
    private var page = 1
    private val source: WebSource<*> = createWebSourceBy(sourceString)

    //    override fun fetchThumbnails() {
//        val source: Available
//        val baseUrl: String
//        val categoryPara: String
//        try {
//            source = getSource()
//            baseUrl = source.getBaseUrl()
//            categoryPara = getCategoryParaFrom(source)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            toast(e)
//            return
//        }
//
//        GalleryClient.getInstance(baseUrl)
//            .getThumbnails(query = categoryPara, page = page)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : io.reactivex.rxjava3.core.Observer<ResponseBody> {
//                override fun onComplete() {
//                    log("onComplete")
//                }
//
//                override fun onSubscribe(d: Disposable) {
//                    log("onSubscribe")
//                }
//
//                override fun onNext(t: ResponseBody) {
//                    val docString = t.string()
//                    log("onNext,doc:$docString")
//                    val thumbnailUrls = source.extractImagesFrom(docString)
//                    thumbnailUrls.forEach { log("onNext,element:$it") }
//                    galleryView?.onFetchingCompleted(thumbnailUrls)
//                    page++
//                }
//
//                override fun onError(e: Throwable) {
//                    log("onError:$e")
//                    toast("onError:$e")
//                }
//
//            })
//    }
//
//    override fun fetchMoreThumbnails() {
//        val source: Available
//        val baseUrl: String
//        val categoryPara: String
//        try {
//            source = getSource()
//            baseUrl = source.getBaseUrl()
//            categoryPara = getCategoryParaFrom(source)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            toast(e)
//            return
//        }
//
//        GalleryClient.getInstance(baseUrl)
//            .getThumbnails(query = categoryPara, page = page)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : io.reactivex.rxjava3.core.Observer<ResponseBody> {
//                override fun onComplete() {
//                    log("onComplete")
//                }
//
//                override fun onSubscribe(d: Disposable) {
//                    log("onSubscribe")
//                    disposable = d
//                }
//
//                override fun onNext(t: ResponseBody) {
//                    val docString = t.string()
//                    log("onNext,doc:$docString")
//                    val thumbnailUrls = source.extractImagesFrom(docString)
//                    thumbnailUrls.forEach { log("onNext,element:$it") }
//                    galleryView?.onFetchingMoreCompleted(thumbnailUrls)
//                    page++
//                }
//
//                override fun onError(e: Throwable) {
//                    log("onError:$e")
//                    toast("onError:$e")
//                }
//
//            })
//    }
    override fun getLatestResults() {
        val observables =
            source.getLatestImageUrls() ?: throw CategoryNotFoundException("latest")
        executeUrls(observables)
    }

    override fun onDestroy() {
        galleryView = null
    }

    private fun executeUrls(observables: Observable<out Fetchable>) {
        observables.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.rxjava3.core.Observer<Fetchable> {
                override fun onComplete() {
                    log("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    log("onSubscribe")
                }

                override fun onNext(t: Fetchable) {
                    val sources = t.getImageSources()
                    sources.forEach { log("onNext,element:\n$it") }
                    galleryView?.onImagesFetched(sources)
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