package gishlabs.exandriapodcast.podcastrepository.remote

import gishlabs.exandriapodcast.podcastrepository.PodcastRepository
import gishlabs.exandriapodcast.podcastrepository.remote.models.Podcasts

class ExandriaPodcastRemoteRepository(private val service: ListenNotesService) : PodcastRepository {

    // IDS
    // 0991703626c048b3ab6c0a4ba7f807f4 -- Critical Role, Exandria Unlimited, UnDeadwood, Talks Machina, and a growing selection of one-shots
    // b7e04fc3ab1e4c488fc5931b7bb7e2d5 -- Critical Role Vox Machina and fist 19 episodes from Mighty Nein
    // bf4813f1fc5a4aa081c831a21228759e -- Between the Sheets

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