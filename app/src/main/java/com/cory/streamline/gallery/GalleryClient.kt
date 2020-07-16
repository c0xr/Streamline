package com.cory.streamline.gallery



import io.reactivex.rxjava3.core.Observable
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory



class GalleryClient private constructor() {
    private val thumbnailPageService: GalleryService

    fun getThumbnails(query: String, page: Int): Observable<ResponseBody> {
        return thumbnailPageService.getThumbnails(query = query,page = page)
    }

    companion object {
        private lateinit var baseUrl: String
        private var instance: GalleryClient? = null
        fun getInstance(baseUrl: String): GalleryClient {
            if (instance == null) {
                this.baseUrl = baseUrl
                instance = GalleryClient()
            }
            return instance!!
        }
    }

    init {
//        val builder = OkHttpClient().newBuilder()
//        builder.addInterceptor { chain ->
//            val newRequest: Request = chain.request().newBuilder()
//                .header("Content-Type","application/x-www-form-urlencoded")
//                .removeHeader("Connection")
//                .build()
//            val names=newRequest.headers().names()
//            names.forEach {
//                log("header -> "+it+" : "+newRequest.header(it))
//            }
//            log("final url -> "+newRequest.url())
//            chain.proceed(newRequest)
//        }
//        val client = builder.build()

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
//            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        thumbnailPageService = retrofit.create(GalleryService::class.java)
    }
}