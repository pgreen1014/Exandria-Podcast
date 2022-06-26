package gishlabs.exandriapodcast.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import gishlabs.exandriapodcast.R
import gishlabs.exandriapodcast.showselction.ShowSelectionFragment
import timber.log.Timber

class ExandriaPodcastsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ShowSelectionFragment>(R.id.fragment_container_view)
            }
        }

    }
}