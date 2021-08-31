package gishlabs.exandriapodcast.podcastrepository

import gishlabs.exandriapodcast.podcastrepository.local.database.PodcastDao
import gishlabs.exandriapodcast.podcastrepository.models.BETWEEN_THE_SHEETS
import gishlabs.exandriapodcast.podcastrepository.models.Podcast
import gishlabs.exandriapodcast.podcastrepository.remote.exceptions.UnsuccessfulHTTPStatusCodeException
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisode
import gishlabs.exandriapodcast.podcastrepository.usecase.AssignShowIdUseCase
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException
import java.lang.RuntimeException

class PodcastDataInitializer(
        private val service: ListenNotesService,
        private val podcastDao: PodcastDao,
        private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    @Throws(UnsuccessfulHTTPStatusCodeException::class)
    suspend fun initialize(onComplete: () -> Unit) {
        withContext(dispatcher) {
            val jobOne = launch { insertAllEpisodesFromPodcastToDB(ListenNotesService.PODCAST_ID_ORIGINAL) }
            val jobTwo = launch { insertAllEpisodesFromPodcastToDB(ListenNotesService.PODCAST_ID_CURRENT) }
            val jobThree = launch { insertAllEpisodesFromPodcastToDB(ListenNotesService.PODCAST_ID_BETWEEN_THE_SHEETS) }
            joinAll(jobOne, jobTwo, jobThree)
            onComplete()
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(IOException::class, RuntimeException::class, UnsuccessfulHTTPStatusCodeException::class)
    private suspend fun insertAllEpisodesFromPodcastToDB(podcastId: String) {
        withContext(dispatcher) {
            var nextEpisodePubDate: Long? = null
            var totalEpisodes = 0
            val params = mutableMapOf(
                Pair("sort", ListenNotesService.SORT_ORDER_OLDEST_FIRST)
            )

            var dbInsertionCount = 0
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

                response.body()?.episodes?.forEach { it ->
                    dbInsertPodcastEpisodeResult(it, podcastId)
                    dbInsertionCount++
                }

            } while(dbInsertionCount < totalEpisodes)
        }
    }

    private fun dbInsertPodcastEpisodeResult(episode: PodcastEpisode, podcastId: String) {
        val showId = if (podcastId == ListenNotesService.PODCAST_ID_BETWEEN_THE_SHEETS) {
            BETWEEN_THE_SHEETS
        } else {
            AssignShowIdUseCase().assignShowId(episode.title)
        }

        Timber.d("Inserting episode to db: ${episode.title} -> showID: $showId")
        val podcast = Podcast(
            id = episode.id,
            episodeTitle = episode.title,
            episodeDescription = episode.description,
            showID = showId,
            publishedDateMillis = episode.publishedDateMillis
        )
        podcastDao.insertPodcast(podcast)
    }
}