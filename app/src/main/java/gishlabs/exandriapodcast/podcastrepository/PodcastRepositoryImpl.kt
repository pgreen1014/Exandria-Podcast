package gishlabs.exandriapodcast.podcastrepository

import gishlabs.exandriapodcast.podcastrepository.local.database.PodcastDao
import gishlabs.exandriapodcast.podcastrepository.models.*
import gishlabs.exandriapodcast.podcastrepository.remote.PodcastServiceRepository
import gishlabs.exandriapodcast.podcastrepository.remote.models.Podcasts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PodcastRepositoryImpl(
        private val podcastsDao: PodcastDao,
        private val remoteRepo: PodcastServiceRepository,
    ): PodcastRepository {

    override suspend fun getAllExandriaPodcasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            val result = podcastsDao.getAllEpisodes()
            callback(result)
        }
    }

    override suspend fun getAllVoxMachinaPodcasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            val result = podcastsDao.getAllEpisodes(VOX_MACHINA_CAMPAIGN)
            callback(result)
        }
    }

    override suspend fun getAllMightyNeinPodcasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            val result = podcastsDao.getAllEpisodes(MIGHTY_NEIN_CAMPAIGN)
            callback(result)
        }
    }

    override suspend fun getAllTalksMachinaPodcasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            val result = podcastsDao.getAllEpisodes(TALKS_MACHINA)
            callback(result)
        }
    }

    override suspend fun getAllExandriaUnlimitedPocasts(callback: (List<Podcast>) -> Unit) {
        withContext(Dispatchers.IO) {
            val result = podcastsDao.getAllEpisodes(EXANDRIA_UNLIMITED)
            callback(result)
        }
    }

    override fun checkForNewEpisodes() {
        TODO("Not yet implemented")
    }

    override fun getAudioForPodcast(id: String, callback: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun deleteDownloadedPodcast(id: String) {
        TODO("Not yet implemented")
    }

    override fun insertPodcast(podcast: Podcast) {
        TODO("Not yet implemented")
    }
}