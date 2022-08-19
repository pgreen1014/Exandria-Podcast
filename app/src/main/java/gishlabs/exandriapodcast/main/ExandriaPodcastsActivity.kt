package gishlabs.exandriapodcast.main

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import gishlabs.exandriapodcast.R
import gishlabs.exandriapodcast.showepisodes.EpisodesFragment
import gishlabs.exandriapodcast.showselction.ShowSelectionFragment

class ExandriaPodcastsActivity : AppCompatActivity(), ShowSelectionFragment.Callbacks {

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

    override fun onShowSelected(
        sharedImage: ImageView,
        transitionName: String,
        showTitle: String,
        imageResourceId: Int
    ) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addSharedElement(sharedImage, EpisodesFragment.TRANSITION_NAME_SHOW_SPLASH)
            replace(R.id.fragment_container_view, EpisodesFragment.newInstance(showTitle, imageResourceId))
            addToBackStack(null)
        }
    }
}