package gishlabs.exandriapodcast.podcastservice

import gishlabs.exandriapodcast.podcastservice.models.PodcastSearchResult
import retrofit2.Call
import retrofit2.http.GET

interface ListenNotesService {

    @GET("search?q=Critical%20Role&type=podcast&language=English")
    fun searchCriticalRolePodcasts(): Call<PodcastSearchResult>
}