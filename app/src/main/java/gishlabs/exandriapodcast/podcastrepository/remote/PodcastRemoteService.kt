package gishlabs.exandriapodcast.podcastrepository.remote

import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisode

interface PodcastRemoteService {

    suspend fun getPodcastOneEpisodes(sortOrder: String,
                                      onSuccess: (episodes: List<PodcastEpisode>) -> Unit,
                                      onFailure: (error: Throwable) -> Unit)

    suspend fun getPodcastTwoEpisodes(sortOrder: String,
                                      onSuccess: (episodes: List<PodcastEpisode>) -> Unit,
                                      onFailure: (error: Throwable) -> Unit)

    suspend fun getBetweenTheSheetsEpisodes(sortOrder: String,
                                            onSuccess: (episodes: List<PodcastEpisode>) -> Unit,
                                            onFailure: (error: Throwable) -> Unit)

    companion object {
        const val SORT_ORDER_RECENT = "recent_first"
        const val SORT_ORDER_OLDEST = "oldest_first"
    }

}