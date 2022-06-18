package gishlabs.exandriapodcast.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import gishlabs.exandriapodcast.R

class ExandriaPodcastsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this).get(ExandriaPodcastsActivityViewModel::class.java)

    }
}