package gishlabs.exandriapodcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gishlabs.exandriapodcast.podcastrepository.PodcastRepository
import gishlabs.exandriapodcast.podcastrepository.PodcastRepositoryImpl
import gishlabs.exandriapodcast.podcastrepository.remote.PodcastServiceImpl
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesServiceBuilder
import kotlinx.coroutines.*

class ExandriaPodcastsActivityViewModel : ViewModel() {

    private val podcastRepository: PodcastRepository

    init {
        val service: ListenNotesService = ListenNotesServiceBuilder().getService()
        val podcastService = PodcastServiceImpl(service, Dispatchers.IO, viewModelScope.coroutineContext)
        podcastRepository = PodcastRepositoryImpl(podcastService)
    }


}