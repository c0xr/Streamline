package com.cory.streamline.gallery


import com.cory.streamline.model.*
import com.cory.streamline.util.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.*
import java.lang.Exception


class GalleryPresenter(
    private var galleryView: IGalleryView?,
    var sourceString: String,
    var category: String
) : IGalleryPresenter {
    private lateinit var baseUrl: String
    private var disposable: Disposable? = null
    private var page = 1

    override fun fetchThumbnails() {
        lateinit var source: Fetchable
        lateinit var categoryPara: String
        try {
            source = getSource()
            baseUrl = source.getBaseUrl()
            categoryPara = getCategoryParaFrom(source)
        } catch (e: Exception) {
            e.printStackTrace()
            toast(e)
            return
        }

        GalleryClient.getInstance(baseUrl)
            .getThumbnails(query = categoryPara, page = page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.rxjava3.core.Observer<ResponseBody> {
                override fun onComplete() {
                    log("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    log("onSubscribe")
                }

                override fun onNext(t: ResponseBody) {
                    val docString = t.string()
                    log("onNext:$docString")
                    val thumbnailUrls = source.extractImagesFrom(docString)
                    thumbnailUrls.forEach { log("onNext:$it") }
                    galleryView?.onFetchingCompleted(thumbnailUrls)
                    page++
                }

                override fun onError(e: Throwable) {
                    log("onError:$e")
                    toast("onError:$e")
                }

            })
    }

    override fun fetchMoreThumbnails() {
        lateinit var source: Fetchable
        lateinit var categoryPara: String
        try {
            source = getSource()
            baseUrl = source.getBaseUrl()
            categoryPara = getCategoryParaFrom(source)
        } catch (e: Exception) {
            e.printStackTrace()
            toast(e)
            return
        }

        GalleryClient.getInstance(baseUrl)
            .getThumbnails(query = categoryPara, page = page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.rxjava3.core.Observer<ResponseBody> {
                override fun onComplete() {
                    log("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    log("onSubscribe")
                    disposable = d
                }

                override fun onNext(t: ResponseBody) {
                    val docString = t.string()
                    log("onNext,doc:$docString")
                    val thumbnailUrls = source.extractImagesFrom(docString)
                    thumbnailUrls.forEach { log("onNext,element:$it") }
                    galleryView?.onFetchingMoreCompleted(thumbnailUrls)
                    page++
                }

                override fun onError(e: Throwable) {
                    log("onError:$e")
                    toast("onError:$e")
                }

            })
    }

    override fun clearReference() {
        galleryView = null
    }

    private fun getSource(): Fetchable =
        when (sourceString) {
            SOURCE_WALLHAVEN -> WallhevenSource()
            SOURCE_PIXABAY -> PixabaySource()
            SOURCE_FREEPIK -> FreepikSource()
            else -> throw SourceNotFoundException(sourceString)
        }

    private fun getCategoryParaFrom(source: Fetchable) =
        when (category) {
            CATEGORY_LATEST -> source.getLatestPara()
            CATEGORY_POPULAR -> source.getPopularPara()
            else -> throw CategoryNotFoundException(category)
        }

    private fun unsubscribeLast() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }
}