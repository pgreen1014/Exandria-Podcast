package gishlabs.exandriapodcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gishlabs.exandriapodcast.podcastrepository.PodcastDataInitializer
import gishlabs.exandriapodcast.podcastrepository.PodcastRepository
import gishlabs.exandriapodcast.podcastrepository.PodcastRepositoryImpl
import gishlabs.exandriapodcast.podcastrepository.remote.PodcastServiceImpl
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesServiceBuilder
import gishlabs.exandriapodcast.repotesters.DatabaseSingleton
import kotlinx.coroutines.*
import timber.log.Timber

class ExandriaPodcastsActivityViewModel : ViewModel() {

    private val repoInitializer: PodcastDataInitializer
    private val podcastRepository: PodcastRepository

    init {
        val service: ListenNotesService = ListenNotesServiceBuilder().getService()
        val db = DatabaseSingleton.get()
        repoInitializer = PodcastDataInitializer(service, db.podcastDao)
        val podcastService = PodcastServiceImpl(service, Dispatchers.IO, viewModelScope.coroutineContext)
        podcastRepository = PodcastRepositoryImpl(db.podcastDao, podcastService)
    }

    fun initializeData(onComplete: () -> Unit) {
        viewModelScope.launch {
            repoInitializer.initialize {
                Timber.d("COMPLETED DATA INITIALIZATION")
                onComplete()
            }
        }
    }

    fun getVoxMachinaEpisodes() {
        viewModelScope.launch {
            podcastRepository.getAllVoxMachinaPodcasts {
                Timber.d("Got Vox Machina episodes ${it.size}")
                it.forEach { episode ->
                    Timber.d(episode.episodeTitle)
                }
            }
        }
    }

    fun getMightyNeinEpisodes() {
        viewModelScope.launch {
            podcastRepository.getAllMightyNeinPodcasts {
                Timber.d("Got Mighty Nein episodes ${it.size}")
                it.forEach { episode ->
                    Timber.d(episode.episodeTitle)
                }
            }
        }
    }

    fun getExandriaUnlimitedEpisodes() {
        viewModelScope.launch {
            podcastRepository.getAllExandriaUnlimitedPocasts {
                Timber.d("Got Exandria Unlimited episodes ${it.size}")
                it.forEach { episode ->
                    Timber.d(episode.episodeTitle)
                }
            }
        }
    }


}