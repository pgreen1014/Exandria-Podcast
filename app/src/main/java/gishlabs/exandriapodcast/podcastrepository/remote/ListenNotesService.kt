package gishlabs.exandriapodcast.podcastrepository.remote

import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisodesResult
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastSearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ListenNotesService {
    companion object {
        const val SORT_ORDER_RECENT_FIRST = "recent_first"
        const val SORT_ORDER_OLDEST_FIRST = "oldest_first"
    }

    @GET("search?q=Critical%20Role&type=podcast&language=English")
    fun searchCriticalRolePodcasts(): Call<PodcastSearchResult>

    @GET("podcasts/b7e04fc3ab1e4c488fc5931b7bb7e2d5")
    fun getOriginalPodcastEpisodes(@Query("sort") sortOrder: String): Call<PodcastEpisodesResult>
}