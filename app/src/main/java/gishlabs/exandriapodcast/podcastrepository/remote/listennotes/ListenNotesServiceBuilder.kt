package gishlabs.exandriapodcast.podcastrepository.remote.listennotes

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListenNotesServiceBuilder() {

    companion object {
        const val baseUrl = "https://listen-api.listennotes.com/api/v2/"
    }

    private fun getHttpClient() = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-ListenAPI-Key", LISTENNOTES_API_KEY)
                .build()
            chain.proceed(request)
        }
        .build()


    fun getService(): ListenNotesService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ListenNotesService::class.java)
}