package gishlabs.exandriapodcast.showselction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import gishlabs.exandriapodcast.R
import gishlabs.exandriapodcast.data.Show

class ShowSelectionAdapter(
    private val shows: List<Show>,
    private val showSelectedListener: OnShowSelectedListener
) : RecyclerView.Adapter<ShowSelectionAdapter.ViewHolder>() {

    interface OnShowSelectedListener {
        fun onShowSelected(sharedView: ImageView, title: String, imageResourceId: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        val backgroundImage: ImageView
        val showContainerView: ConstraintLayout

        init {
            titleTextView = view.findViewById(R.id.show_title)
            showContainerView = view.findViewById(R.id.show_container)
            backgroundImage = view.findViewById(R.id.show_image_view)
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
            showSelectedListener.onShowSelected(holder.backgroundImage, holder.titleTextView.text.toString(), shows[position].imageResourceID)
        }
    }

    override fun getItemCount() = shows.size
}