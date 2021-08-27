package gishlabs.exandriapodcast.podcastrepository.remote

import android.util.Log
import gishlabs.exandriapodcast.podcastrepository.remote.exceptions.UnsuccessfulNetworkResponseException
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ExandriaPodcastRemoteRepository(
    private val service: ListenNotesService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : PodcastRemoteService {

    companion object {
        const val TAG = "PodcastRemoteRepo"
    }

    override suspend fun getPodcastOneEpisodes(sortOrder: String,
                                               onSuccess: (episodes: List<PodcastEpisode>) -> Unit,
                                               onFailure: (error: Throwable) -> Unit) {
        try {
            val episodes = getAllPodcastsEpisodes(ListenNotesService.PODCAST_ID_ORIGINAL, sortOrder)
            onSuccess(episodes)
        } catch (exception: Exception) {
            onFailure(exception)
        }
    }

    override suspend fun getPodcastTwoEpisodes(sortOrder: String,
                                               onSuccess: (episodes: List<PodcastEpisode>) -> Unit,
                                               onFailure: (error: Throwable) -> Unit) {
        try {
            val episodes = getAllPodcastsEpisodes(ListenNotesService.PODCAST_ID_CURRENT, sortOrder)
            onSuccess(episodes)
        } catch (exception: Exception) {
            onFailure(exception)
        }
    }

    override suspend fun getBetweenTheSheetsEpisodes(sortOrder: String,
                                                     onSuccess: (episodes: List<PodcastEpisode>) -> Unit,
                                                     onFailure: (error: Throwable) -> Unit) {
        try {
            val episodes = getAllPodcastsEpisodes(ListenNotesService.PODCAST_ID_BETWEEN_THE_SHEETS, sortOrder)
            onSuccess(episodes)
        } catch (exception: Exception) {
            onFailure(exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getAllPodcastsEpisodes(podcastId: String, sortOrder: String): List<PodcastEpisode> {
        return withContext(dispatcher) {
            var nextEpisodePubDate: Long? = null
            var totalEpisodes = 0
            val params = mutableMapOf(
                Pair("sort", sortOrder)
            )
            val episodes: MutableList<PodcastEpisode> = mutableListOf()

            do {
                if (nextEpisodePubDate != null) params["next_episode_pub_date"] = nextEpisodePubDate.toString()

                val call = service.getPodcastEpisodes(podcastId, params)
                val response = call.execute()
                if (!response.isSuccessful) {
                    val errorMessage = "Failed to get podcast episodes: ${response.code()}\n${call.request().url()}"
                    throw UnsuccessfulNetworkResponseException(errorMessage)
                }

                response.body()?.let {
                    totalEpisodes = it.totalEpisodes
                    nextEpisodePubDate = it.nextEpisodePubDate
                }

                response.body()?.episodes?.let { it ->
                    it.forEach { Log.d(TAG, "Adding ${it.title} to episode result list") }
                    episodes.addAll(it)
                }

            } while(episodes.size < totalEpisodes)

            episodes
        }
    }

}