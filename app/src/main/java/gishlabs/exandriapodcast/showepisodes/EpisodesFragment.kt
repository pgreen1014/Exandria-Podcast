package gishlabs.exandriapodcast.showepisodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import gishlabs.exandriapodcast.R
import gishlabs.exandriapodcast.databinding.ActivityEpisodesBinding

class EpisodesFragment: Fragment() {

    private var _binding: ActivityEpisodesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ActivityEpisodesBinding.inflate(inflater, container, false)
        val view = binding.root
        arguments?.let {
            binding.toolbar.title = it.getString(TITLE)
            binding.toolbarShowImage.setImageResource(it.getInt(IMAGE_ID))
        }
        initEpisodesRecyclerView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.toolbarShowImage, TRANSITION_NAME_SHOW_SPLASH)
    }

    private fun initEpisodesRecyclerView() {
        val episodes = mutableListOf<String>()
        for (i in 1..100) {
            episodes.add("Episode $i")
        }

        binding.episodesRecyclerView.adapter = EpisodeListAdapter(episodes)
        binding.episodesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        private const val TITLE = "show_episodes_title"
        private const val IMAGE_ID = "show_episodes_image_id"
        const val TRANSITION_NAME_SHOW_SPLASH = "show_splash"

        fun newInstance(title: String, imageResourceId: Int): EpisodesFragment {
            val args = Bundle()
            args.putString(TITLE, title)
            args.putInt(IMAGE_ID, imageResourceId)

            val frag = EpisodesFragment()
            frag.arguments = args
            return frag
        }
    }

}