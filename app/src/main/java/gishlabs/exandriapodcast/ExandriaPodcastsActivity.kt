package gishlabs.exandriapodcast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import gishlabs.exandriapodcast.repotesters.DatabaseSingleton


class ExandriaPodcastsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DatabaseSingleton.initialize(this)

        val viewModel = ViewModelProvider(this).get(ExandriaPodcastsActivityViewModel::class.java)
        viewModel.initializeData {
//            viewModel.getVoxMachinaEpisodes()
//            viewModel.getMightyNeinEpisodes()
            viewModel.getExandriaUnlimitedEpisodes()
        }

    }
}