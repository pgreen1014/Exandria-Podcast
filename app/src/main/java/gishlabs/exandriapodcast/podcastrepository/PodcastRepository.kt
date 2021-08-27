package gishlabs.exandriapodcast.podcastrepository

import gishlabs.exandriapodcast.podcastrepository.remote.models.Podcasts

interface PodcastRepository {
    fun initializeData()
    fun getAllExandriaPodcasts(callback: () -> List<Podcasts>)
    fun getAllVoxMachinaPodcasts(callback: () -> List<Podcasts>)
    fun getAllMightyNeinPodcasts(callback: () -> List<Podcasts>)
    fun getAllTalksMachinaPodcasts(callback: () -> List<Podcasts>)
    fun getAllExandriaUnlimitedPocasts(callback: () -> List<Podcasts>)
    fun getRecentPodcasts(callback: () -> List<Podcasts>)
    fun getAudioForPodcast(id: String, callback: () -> Unit)
    fun deleteDownloadedPodcast(id: String)
}