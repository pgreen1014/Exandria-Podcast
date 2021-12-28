package gishlabs.exandriapodcast.podcastrepository

import gishlabs.exandriapodcast.podcastrepository.models.Podcast
import gishlabs.exandriapodcast.podcastrepository.remote.models.Podcasts

interface PodcastRepository {
    suspend fun getAllExandriaPodcasts(callback: (List<Podcast>) -> Unit)
    suspend fun getAllVoxMachinaPodcasts(callback: (List<Podcast>) -> Unit)
    suspend fun getAllMightyNeinPodcasts(callback: (List<Podcast>) -> Unit)
    suspend fun getAllTalksMachinaPodcasts(callback: (List<Podcast>) -> Unit)
    suspend fun getAllExandriaUnlimitedPocasts(callback: (List<Podcast>) -> Unit)
    suspend fun getAllBetweenTheSheetsPodcasts(callback: (List<Podcast>) -> Unit)
    fun getAudioForPodcast(id: String, callback: () -> Unit)
    fun deleteDownloadedPodcast(id: String)
}