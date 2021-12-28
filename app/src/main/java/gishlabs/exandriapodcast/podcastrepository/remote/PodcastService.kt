package gishlabs.exandriapodcast.podcastrepository.remote

import gishlabs.exandriapodcast.podcastrepository.models.Podcast

interface PodcastService {

    suspend fun getVoxMachinaEpisodes(sortOrder: String): List<Podcast>
    suspend fun getMightyNeinEpisodes(sortOrder: String): List<Podcast>
    suspend fun getExandriaUnlimitedEpisodes(sortOrder: String): List<Podcast>
    suspend fun getNewEpisodes(showID: Int, latestEpisodeInDB: Podcast, onNewEpisodesFound: (List<Podcast>) -> Unit)
    suspend fun checkForUpdates(showID: Int, latestepisodeInDB: Podcast, onNewEpisodesFound: (newEpisodes: List<Podcast>) -> Unit)
}