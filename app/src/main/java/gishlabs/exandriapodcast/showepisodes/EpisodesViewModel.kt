package gishlabs.exandriapodcast.showepisodes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EpisodesViewModel : ViewModel() {

    val episodes: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    fun loadEpisodes() {
        val initialEpisodes = mutableListOf<String>()
        for (i in 1..100) {
            initialEpisodes.add("Episode $i")
        }

        episodes.value = initialEpisodes
    }

}