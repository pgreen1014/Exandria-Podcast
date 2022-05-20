package gishlabs.exandriapodcast.podcastrepository.remote

import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PodcastServiceImpl(
        private val service: ListenNotesService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
        override val coroutineContext: CoroutineContext
) : PodcastService, CoroutineScope {


}