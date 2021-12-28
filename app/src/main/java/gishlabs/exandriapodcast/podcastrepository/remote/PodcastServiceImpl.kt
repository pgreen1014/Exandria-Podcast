package gishlabs.exandriapodcast.podcastrepository.remote

import gishlabs.exandriapodcast.podcastrepository.models.*
import gishlabs.exandriapodcast.podcastrepository.remote.exceptions.UnsuccessfulHTTPStatusCodeException
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisode
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisodesResult
import gishlabs.exandriapodcast.podcastrepository.usecase.AssignShowIdUseCase
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

class PodcastServiceImpl(
        private val service: ListenNotesService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
        override val coroutineContext: CoroutineContext
) : PodcastService, CoroutineScope {

    override suspend fun getVoxMachinaEpisodes(sortOrder: String): List<Podcast> {
        return getEpisodesFromPodcastAsync(ListenNotesService.PODCAST_ID_ORIGINAL, sortOrder) {
            if (!it.title.contains("Vox Machina"))
                null
            else
                it.toPodcast(VOX_MACHINA_CAMPAIGN)
        }.await()
    }

    override suspend fun getMightyNeinEpisodes(sortOrder: String): List<Podcast> {
        val currentPodcastResult = getEpisodesFromPodcastAsync(ListenNotesService.PODCAST_ID_CURRENT, sortOrder) {
            if (!it.title.contains("C2"))
                null
            else it.toPodcast(MIGHTY_NEIN_CAMPAIGN)
        }
        val originalPodcastResult = getEpisodesFromPodcastAsync(ListenNotesService.PODCAST_ID_ORIGINAL, sortOrder) {
            if (!it.title.contains("C2"))
                null
            else it.toPodcast(MIGHTY_NEIN_CAMPAIGN)
        }

        return originalPodcastResult.await() + currentPodcastResult.await()
    }

    override suspend fun getExandriaUnlimitedEpisodes(sortOrder: String): List<Podcast> {
        return getEpisodesFromPodcastAsync(ListenNotesService.PODCAST_ID_CURRENT, sortOrder) {
            if (!it.title.contains("Exandria Unlimited"))
                null
            else it.toPodcast(EXANDRIA_UNLIMITED)
        }.await()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(UnsuccessfulHTTPStatusCodeException::class)
    override suspend fun getNewEpisodes(
        showID: Int,
        latestEpisodeInDB: Podcast,
        onNewEpisodesFound: (List<Podcast>) -> Unit
    ) = withContext(ioDispatcher) {
        var nextEpisodePubDate: Long? = null
        var totalEpisodesInPodcast = 0
        val requestParams = mutableMapOf(
            Pair("sort", ListenNotesService.SORT_ORDER_RECENT_FIRST)
        )
        val podcastIDs = getPodcastIDsForShow(showID)
        var currentPodcastIterating = podcastIDs.removeFirstOrNull()

        val newEpisodes = mutableListOf<Podcast>()
        var iteratedEpisodesInPodcast = 0
        var isDbUpdated = false
        do {
            if (nextEpisodePubDate != null) requestParams["next_episode_pub_date"] = nextEpisodePubDate.toString()
            if (currentPodcastIterating == null) break
            val call = service.getPodcastEpisodes(currentPodcastIterating, requestParams)
            val response = call.execute()

            checkForHTTPError(call, response)
            response.body()?.let {
                totalEpisodesInPodcast = it.totalEpisodes
                nextEpisodePubDate = it.nextEpisodePubDate

                it.episodes.forEach { podcastEpisode ->
                    val episodeShowID = if (showID == BETWEEN_THE_SHEETS) {
                        BETWEEN_THE_SHEETS
                    } else {
                        AssignShowIdUseCase().assignShowId(podcastEpisode.title)
                    }
                    if (podcastEpisode.id == latestEpisodeInDB.id) {
                        isDbUpdated = true
                        return@let
                    }
                    else if (episodeShowID == showID) {
                        val newEpisode = podcastEpisode.toPodcast(showID)
                        newEpisodes.add(newEpisode)
                    }
                    iteratedEpisodesInPodcast++
                }
            }

            if (iteratedEpisodesInPodcast == totalEpisodesInPodcast && !isDbUpdated) {
                currentPodcastIterating = podcastIDs.removeFirstOrNull()
                iteratedEpisodesInPodcast = 0
            }

        } while (!isDbUpdated && currentPodcastIterating != null)

        onNewEpisodesFound(newEpisodes)
    }

    override suspend fun checkForUpdates(
        showID: Int,
        latestepisodeInDB: Podcast,
        onNewEpisodesFound: (newEpisodes: List<Podcast>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    /**
     * @param podcastId: id for the podcast identifier on the remote service
     * @param sortOrder: the sort order for the service. May cause issues if combined with latestEpisodeInDB parameter.
     * @param latestEpisodeInDB: the latest episode the app has stored in the local database.
     * If null it fetches all episodes, otherwise it will stop iterating when the the service call find this episode
     * @param evaluateEpisode a lambda that determines which episodes to return. If it returns null getEpisodesFromPodcast will to not include it in the return value.
     *
     * @throws UnsuccessfulHTTPStatusCodeException when response returns an unsuccessful HTTP status code
     * @return a list of Podcast episodes
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    private fun getEpisodesFromPodcastAsync(
            podcastId: String,
            sortOrder: String = ListenNotesService.SORT_ORDER_RECENT_FIRST,
            latestEpisodeInDB: Podcast? = null,
            evaluateEpisode: (episode: PodcastEpisode) -> Podcast?
    ) = async(ioDispatcher) {
        var nextEpisodePubDate: Long? = null
        var totalEpisodes = 0
        val params = mutableMapOf(
            Pair("sort", sortOrder)
        )

        val retrievedEpisodes: MutableList<Podcast> = mutableListOf()
        var iteratedEpisode = 0
        var iteratedUpToMostRecentEpisode = false
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

            response.body()?.episodes?.forEach {
                iteratedEpisode++
                if (latestEpisodeInDB?.id == it.id) {
                    iteratedUpToMostRecentEpisode = true
                    return@forEach
                }
                val podcast = evaluateEpisode(it)
                if (podcast != null) retrievedEpisodes.add(podcast)
            }
        } while (shouldContinueIterating(iteratedEpisode, totalEpisodes, iteratedUpToMostRecentEpisode, latestEpisodeInDB))

        return@async retrievedEpisodes
    }

    private fun shouldContinueIterating(iteratedEpisodes: Int, totalEpisodes: Int, hasIteratedToMostRecentEpisode: Boolean, latestEpisodeInDB: Podcast?): Boolean {
        return if (latestEpisodeInDB != null) (iteratedEpisodes < totalEpisodes || !hasIteratedToMostRecentEpisode) else (iteratedEpisodes < totalEpisodes)
    }

    private fun getPodcastIDsForShow(showID: Int): ArrayDeque<String> {
        return when(showID) {
            VOX_MACHINA_CAMPAIGN -> {
                ArrayDeque(listOf(
                    ListenNotesService.PODCAST_ID_ORIGINAL)
                )
            }
            MIGHTY_NEIN_CAMPAIGN -> {
                ArrayDeque(listOf(
                    ListenNotesService.PODCAST_ID_CURRENT,
                    ListenNotesService.PODCAST_ID_ORIGINAL)
                )
            }
            TALKS_MACHINA -> {
                ArrayDeque(listOf(
                    ListenNotesService.PODCAST_ID_CURRENT,
                    ListenNotesService.PODCAST_ID_ORIGINAL)
                )
            }
            BETWEEN_THE_SHEETS -> {
                ArrayDeque(listOf(
                    ListenNotesService.PODCAST_ID_BETWEEN_THE_SHEETS)
                )
            }
            ONE_SHOTS_AND_MISC -> {
                ArrayDeque(listOf(
                    ListenNotesService.PODCAST_ID_CURRENT,
                    ListenNotesService.PODCAST_ID_ORIGINAL)
                )
            }
            EXANDRIA_UNLIMITED -> {
                ArrayDeque(listOf(
                    ListenNotesService.PODCAST_ID_CURRENT)
                )
            }
            else -> {
                ArrayDeque(listOf(
                    ListenNotesService.PODCAST_ID_CURRENT)
                )
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(IOException::class, RuntimeException::class, UnsuccessfulHTTPStatusCodeException::class)
    private suspend fun getAllPodcastEpisodes(podcastId: String, sortOrder: String): List<PodcastEpisode> {
        return withContext(ioDispatcher) {
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
                checkForHTTPError(call, response)

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

   @Throws(UnsuccessfulHTTPStatusCodeException::class)
    private fun checkForHTTPError(call: Call<PodcastEpisodesResult>, response: Response<PodcastEpisodesResult>) {
        if (!response.isSuccessful) {
            val errorMessage = "Failed to get podcast episodes: ${response.code()}\n${call.request().url()}"
            Timber.e(errorMessage)
            throw UnsuccessfulHTTPStatusCodeException(errorMessage)
        }
    }

    private fun PodcastEpisode.toPodcast(showID: Int): Podcast {
        return Podcast(
            id = id,
            episodeTitle = title,
            episodeDescription = description,
            showID = showID,
            publishedDateMillis = publishedDateMillis
        )
    }


}