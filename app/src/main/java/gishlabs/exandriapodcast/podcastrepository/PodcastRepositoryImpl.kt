package gishlabs.exandriapodcast.podcastrepository

import gishlabs.exandriapodcast.podcastrepository.remote.models.Podcasts

class PodcastRepositoryImpl(
    private val localRepo: PodcastRepository,
    private val remoteRepo: PodcastRepository
    ): PodcastRepository {

//    companion object {
//        private const val DATABASE_NAME = "exandria_podcast_database"
//    }
//
//    private val database : ExandriaPodcastDatabase = Room.databaseBuilder(
//        context,
//        ExandriaPodcastDatabase::class.java,
//        DATABASE_NAME
//    ).build()

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
}