package gishlabs.exandriapodcast.podcastrepository.remote

import gishlabs.exandriapodcast.podcastrepository.remote.exceptions.UnsuccessfulHTTPStatusCodeException
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException

class GetEpisodesByPodcastServiceImpl(
    private val service: ListenNotesService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : EpisodesByPodcastService {

    companion object {
        const val TAG = "PodcastRemoteRepo"
    }

    override suspend fun getPodcastOneEpisodes(sortOrder: String,
                                               onSuccess: (episodes: List<PodcastEpisode>) -> Unit,
                                               onFailure: (error: Throwable) -> Unit) {
        try {
            val episodes = getAllPodcastEpisodes(ListenNotesService.PODCAST_ID_ORIGINAL, sortOrder)
            onSuccess(episodes)
        } catch (exception: Exception) {
            onFailure(exception)
        }
    }

    override suspend fun getPodcastTwoEpisodes(sortOrder: String,
                                               onSuccess: (episodes: List<PodcastEpisode>) -> Unit,
                                               onFailure: (error: Throwable) -> Unit) {
        try {
            val episodes = getAllPodcastEpisodes(ListenNotesService.PODCAST_ID_CURRENT, sortOrder)
            onSuccess(episodes)
        } catch (exception: Exception) {
            onFailure(exception)
        }
    }

    override suspend fun getBetweenTheSheetsEpisodes(sortOrder: String,
                                                     onSuccess: (episodes: List<PodcastEpisode>) -> Unit,
                                                     onFailure: (error: Throwable) -> Unit) {
        try {
            val episodes = getAllPodcastEpisodes(ListenNotesService.PODCAST_ID_BETWEEN_THE_SHEETS, sortOrder)
            onSuccess(episodes)
        } catch (exception: Exception) {
            onFailure(exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(IOException::class, RuntimeException::class, UnsuccessfulHTTPStatusCodeException::class)
    private suspend fun getAllPodcastEpisodes(podcastId: String, sortOrder: String): List<PodcastEpisode> {
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
                    Timber.e(errorMessage)
                    throw UnsuccessfulHTTPStatusCodeException(errorMessage)
                }

                response.body()?.let {
                    totalEpisodes = it.totalEpisodes
                    nextEpisodePubDate = it.nextEpisodePubDate
                }

                response.body()?.episodes?.let { it ->
                    episodes.addAll(it)
                }

            } while(episodes.size < totalEpisodes)

            episodes
        }
    }

}