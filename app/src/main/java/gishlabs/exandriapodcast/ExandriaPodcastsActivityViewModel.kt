package gishlabs.exandriapodcast

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gishlabs.exandriapodcast.podcastrepository.remote.ExandriaPodcastRemoteRepository
import gishlabs.exandriapodcast.podcastrepository.remote.ListenNotesService
import gishlabs.exandriapodcast.podcastrepository.remote.ListenNotesServiceBuilder
import gishlabs.exandriapodcast.podcastrepository.remote.PodcastRemoteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExandriaPodcastsActivityViewModel : ViewModel() {

    private val remoteRepo: PodcastRemoteService

    init {
        val service: ListenNotesService = ListenNotesServiceBuilder().getService()
        remoteRepo = ExandriaPodcastRemoteRepository(service, Dispatchers.IO)
    }

    fun loadCampaignOneEpisodes() {
        viewModelScope.launch {
            remoteRepo.getPodcastOneEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST,
                onSuccess = { episodes ->
                    episodes.forEach { Log.d("VoxMachina", it.title)}
                },
                onFailure = { error ->
                    Log.e("ExandriaViewModel", "damn we got an error", error)
                })
        }
    }

    fun loadCampaignTwoEpisodes() {
        viewModelScope.launch {
            remoteRepo.getPodcastTwoEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST,
                onSuccess = { episodes ->
                    episodes.forEach { Log.d("MightyNein", it.title) }
                },
                onFailure = { error ->
                    Log.e("MightyNein", "ugh an error", error)
                })
        }
    }

    fun loadBetweenTheSheets() {
        viewModelScope.launch {
            remoteRepo.getBetweenTheSheetsEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST,
                onSuccess = { episodes ->
                    episodes.forEach { Log.d("BetweenTheSheets", it.title) }
                },
                onFailure = { error ->
                    Log.e("BetweenTheSheets", "ugh an error", error)
                })
        }
    }
}