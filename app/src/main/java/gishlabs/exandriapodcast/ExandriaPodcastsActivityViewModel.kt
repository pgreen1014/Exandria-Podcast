package gishlabs.exandriapodcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gishlabs.exandriapodcast.podcastrepository.remote.GetEpisodesByPodcastServiceImpl
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesServiceBuilder
import gishlabs.exandriapodcast.podcastrepository.remote.EpisodesByPodcastService
import kotlinx.coroutines.*
import timber.log.Timber

class ExandriaPodcastsActivityViewModel : ViewModel() {

    private val repoEpisodesBy: EpisodesByPodcastService
    private val TAG = "coroutines test"

    init {
        val service: ListenNotesService = ListenNotesServiceBuilder().getService()
        repoEpisodesBy = GetEpisodesByPodcastServiceImpl(service, Dispatchers.IO)
    }

    fun loadCampaignOneEpisodes() {
        viewModelScope.launch {
            repoEpisodesBy.getPodcastOneEpisodes(
                ListenNotesService.SORT_ORDER_OLDEST_FIRST,
                onSuccess = { episodes ->
                    episodes.forEach { Timber.d( it.title)}
                },
                onFailure = { error ->
                    Timber.e(error, "damn we got an error")
                })
        }
    }

    fun loadCampaignTwoEpisodes() {
        viewModelScope.launch {
            repoEpisodesBy.getPodcastTwoEpisodes(
                ListenNotesService.SORT_ORDER_OLDEST_FIRST,
                onSuccess = { episodes ->
                    episodes.forEach { Timber.d( it.title) }
                },
                onFailure = { error ->
                    Timber.e(error, "MightyNein", "ugh an error")
                })
        }
    }

    fun loadBetweenTheSheets() {
        viewModelScope.launch {
            repoEpisodesBy.getBetweenTheSheetsEpisodes(
                ListenNotesService.SORT_ORDER_OLDEST_FIRST,
                onSuccess = { episodes ->
                    episodes.forEach { Timber.d( it.title) }
                },
                onFailure = { error ->
                    Timber.e(error,  "ugh an error")
                })
        }
    }
}