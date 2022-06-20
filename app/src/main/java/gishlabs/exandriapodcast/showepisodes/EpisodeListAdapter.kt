package gishlabs.exandriapodcast.showepisodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gishlabs.exandriapodcast.R

class EpisodeListAdapter(private val episodes: List<String>) : RecyclerView.Adapter<EpisodeListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val episodeTitle: TextView

        init {
            episodeTitle = view.findViewById(R.id.episode_title)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.episodeTitle.text = episodes[position]
    }

    override fun getItemCount() = episodes.size


}