package gishlabs.exandriapodcast.showselction

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import gishlabs.exandriapodcast.R
import gishlabs.exandriapodcast.showepisodes.EpisodesActivity

class ShowSelectionAdapter(private val shows: List<String>) : RecyclerView.Adapter<ShowSelectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        private val showContainerView: ConstraintLayout

        init {
            titleTextView = view.findViewById(R.id.show_title)
            showContainerView = view.findViewById(R.id.show_container)
            showContainerView.setOnClickListener {
                val intent = Intent(view.context, EpisodesActivity::class.java).apply {
                    putExtra(EpisodesActivity.ARG_SHOW_TITLE, titleTextView.text)
                }
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = shows[position]
    }

    override fun getItemCount() = shows.size
}