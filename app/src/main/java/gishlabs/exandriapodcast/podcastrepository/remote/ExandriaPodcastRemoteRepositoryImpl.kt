package gishlabs.exandriapodcast.podcastrepository.remote

import gishlabs.exandriapodcast.podcastrepository.PodcastRepository
import gishlabs.exandriapodcast.podcastrepository.models.Podcast
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import gishlabs.exandriapodcast.podcastrepository.remote.models.Podcasts
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


//TODO make remote repository a subclass for initializing data?
class ExandriaPodcastRemoteRepositoryImpl(
    private val listenNotesService: ListenNotesService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): PodcastRepository {

    override fun initializeData() {
        TODO("Not yet implemented")
    }

    override fun getAllExandriaPodcasts(callback: () -> List<Podcasts>) {
        TODO("Not yet implemented")
    }

    override fun getAllVoxMachinaPodcasts(callback: () -> List<Podcasts>) {
        TODO("Not yet implemented")
    }

    override fun getAllMightyNeinPodcasts(callback: () -> List<Podcasts>) {
        TODO("Not yet implemented")
    }

    override fun getAllTalksMachinaPodcasts(callback: () -> List<Podcasts>) {
        TODO("Not yet implemented")
    }

    override fun getAllExandriaUnlimitedPocasts(callback: () -> List<Podcasts>) {
        TODO("Not yet implemented")
    }

    override fun getRecentPodcasts(callback: () -> List<Podcasts>) {
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