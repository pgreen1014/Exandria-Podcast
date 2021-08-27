package gishlabs.exandriapodcast.podcastrepository.remote.listennotes

import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisodesResult
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastSearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ListenNotesService {
    companion object {
        const val SORT_ORDER_RECENT_FIRST = "recent_first"
        const val SORT_ORDER_OLDEST_FIRST = "oldest_first"
        const val PODCAST_ID_ORIGINAL = "b7e04fc3ab1e4c488fc5931b7bb7e2d5"
        const val PODCAST_ID_CURRENT = "0991703626c048b3ab6c0a4ba7f807f4"
        const val PODCAST_ID_BETWEEN_THE_SHEETS = "bf4813f1fc5a4aa081c831a21228759e"
    }

    @GET("search?q=Critical%20Role&type=podcast&language=English")
    fun searchCriticalRolePodcasts(): Call<PodcastSearchResult>

    @GET("podcasts/{podcastId}")
    fun getPodcastEpisodes(
        @Path("podcastId") podcastId: String,
        @QueryMap params: Map<String, String>
    ): Call<PodcastEpisodesResult>
}