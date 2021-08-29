package gishlabs.exandriapodcast.podcastrepository.local

import gishlabs.exandriapodcast.podcastrepository.PodcastRepository
import gishlabs.exandriapodcast.podcastrepository.models.Podcast
import gishlabs.exandriapodcast.podcastrepository.remote.models.Podcasts

class ExandriaPodcastLocalRepository : PodcastRepository{

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