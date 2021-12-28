package gishlabs.exandriapodcast.podcastrepository

import gishlabs.exandriapodcast.podcastrepository.local.database.PodcastDao
import gishlabs.exandriapodcast.podcastrepository.models.*
import gishlabs.exandriapodcast.podcastrepository.remote.PodcastService
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

const val EXPECTED_VOX_MACHINA_EPISODES = 116
const val EXPECTED_MIGHTY_NEIN_EPISODES = 141
const val EXPECTED_EXANDRIA_UNLIMITED_EPISODES = 9

class PodcastRepositoryImpl(
    private val podcastsDao: PodcastDao,
    private val remoteRepo: PodcastService,
    ): PodcastRepository {

    override suspend fun getAllExandriaPodcasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            val result = podcastsDao.getAllEpisodes()
            callback(result)
        }
    }

    override suspend fun getAllVoxMachinaPodcasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            var result = podcastsDao.getAllEpisodes(VOX_MACHINA_CAMPAIGN)

            if (result.size != EXPECTED_VOX_MACHINA_EPISODES) {
                Timber.d("Incorrect number of vox machina episodes. Syncing data with the server...")
                result = remoteRepo.getVoxMachinaEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST)
                podcastsDao.insertEpisodes(result)
            }

            callback(result)
        }
    }

    override suspend fun getAllMightyNeinPodcasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            var result = podcastsDao.getAllEpisodes(MIGHTY_NEIN_CAMPAIGN)

            if (result.size != EXPECTED_MIGHTY_NEIN_EPISODES) {
                Timber.d("Incorrect number of mighty nein episodes. Syncing data with the server")
                result = remoteRepo.getMightyNeinEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST)
                podcastsDao.insertEpisodes(result)
            }

            callback(result)
        }
    }

    override suspend fun getAllTalksMachinaPodcasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            val result = podcastsDao.getAllEpisodes(TALKS_MACHINA)
            callback(result)

            val mostRecentEpisode = result[result.size - 1]   // TODO this will need to be dynamic as the user choose sort order
            launch {
                remoteRepo.checkForUpdates(TALKS_MACHINA, mostRecentEpisode) { newEpisodes ->
                    callback(newEpisodes)
                }
            }
        }
    }

    override suspend fun getAllExandriaUnlimitedPocasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            var result = podcastsDao.getAllEpisodes(EXANDRIA_UNLIMITED)

            if (result.size != EXPECTED_EXANDRIA_UNLIMITED_EPISODES) {
                Timber.d("Incorrect number of Exandria Unlimited episodes. Syncing data with the server")
                result = remoteRepo.getExandriaUnlimitedEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST)
                podcastsDao.insertEpisodes(result)
            }

            callback(result)
        }
    }

    override suspend fun getAllBetweenTheSheetsPodcasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            val result = podcastsDao.getAllEpisodes(BETWEEN_THE_SHEETS)
            callback(result)

            val mostRecentEpisode = result[result.size -1]
            launch {
                remoteRepo.checkForUpdates(BETWEEN_THE_SHEETS, mostRecentEpisode) { newEpisodes ->
                    callback(newEpisodes)
                }
            }
        }
    }

    override fun getAudioForPodcast(id: String, callback: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun deleteDownloadedPodcast(id: String) {
        TODO("Not yet implemented")
    }

}