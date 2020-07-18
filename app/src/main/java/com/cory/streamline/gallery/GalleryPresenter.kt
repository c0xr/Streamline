package com.cory.streamline.gallery


import com.cory.streamline.model.web.repo.WallhevenRepo
import com.cory.streamline.model.web.source.WallhevenSource
import com.cory.streamline.util.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalStateException


class GalleryPresenter(
    private var galleryView: IGalleryView?,
    var sourceString: String,
    var category: String
) : IGalleryPresenter {
    private var disposable: Disposable? = null
    private var page = 1

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
    override fun getLastedResults() {
        val source=WallhevenSource()
        val observables=
            source.getLatestImageUrls() ?: throw IllegalStateException()

        observables.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.rxjava3.core.Observer<WallhevenRepo> {
                override fun onComplete() {
                    log("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    log("onSubscribe")
                }

                override fun onNext(t: WallhevenRepo) {
                    val thumbnailUrls = t.getImageSources().map { it.thumbnailImage }
                    thumbnailUrls.forEach { log("onNext,element:$it") }
                    galleryView?.onFetchingCompleted(thumbnailUrls)
                }

                override fun onError(e: Throwable) {
                    log("onError:$e")
                    toast("onError:$e")
                }

            })
    }

    override fun onDestroy() {
        galleryView = null
    }

    private fun unsubscribeLast() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }
}