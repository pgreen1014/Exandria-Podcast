package gishlabs.exandriapodcast.showepisodes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import gishlabs.exandriapodcast.databinding.ActivityEpisodesBinding
import timber.log.Timber

class EpisodesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEpisodesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        intent.extras?.let {
            supportActionBar?.title = it.getString(ARG_SHOW_TITLE)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initEpisodesRecyclerView()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Timber.d("override back")
                finishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finishAfterTransition()
    }

    private fun initEpisodesRecyclerView() {
        val episodes = mutableListOf<String>()
        for (i in 1..100) {
            episodes.add("Episode $i")
        }

        binding.episodesRecyclerView.adapter = EpisodeListAdapter(episodes)
        binding.episodesRecyclerView.layoutManager = LinearLayoutManager(baseContext)
    }

    companion object {
       const val ARG_SHOW_TITLE = "gishlabs.exandriapodcast.showepisodes.show_title"
    }
}