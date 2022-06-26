package gishlabs.exandriapodcast.showselction

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import gishlabs.exandriapodcast.R
import gishlabs.exandriapodcast.data.Show
import gishlabs.exandriapodcast.showepisodes.EpisodesActivity

class ShowSelectionAdapter(private val shows: List<Show>) : RecyclerView.Adapter<ShowSelectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        val backgroundImage: ImageView
        val showContainerView: ConstraintLayout

        init {
            titleTextView = view.findViewById(R.id.show_title)
            showContainerView = view.findViewById(R.id.show_container)
            backgroundImage = view.findViewById(R.id.show_image_view)
            showContainerView.setOnClickListener {
                val intent = Intent(view.context, EpisodesActivity::class.java).apply {
                    putExtra(EpisodesActivity.ARG_SHOW_TITLE, titleTextView.text)
                }
                val options = ActivityOptions.makeSceneTransitionAnimation(view.context as Activity, backgroundImage, "show_background_image")
                view.context.startActivity(intent, options.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = shows[position].name
        holder.backgroundImage.setImageResource(shows[position].imageResourceID)

        holder.showContainerView.setOnClickListener {
            val intent = Intent(holder.showContainerView.context, EpisodesActivity::class.java).apply {
                putExtra(EpisodesActivity.ARG_SHOW_TITLE, holder.titleTextView.text)
                putExtra(EpisodesActivity.ARG_SHOW_IMAGE_RESOURCE_ID, shows[position].imageResourceID)
            }
            val options = ActivityOptions.makeSceneTransitionAnimation(holder.showContainerView.context as Activity, holder.backgroundImage, "show_background_image")
            holder.showContainerView.context.startActivity(intent, options.toBundle())
        }
    }

    override fun getItemCount() = shows.size
}