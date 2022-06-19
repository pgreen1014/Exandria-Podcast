package gishlabs.exandriapodcast.showselction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import gishlabs.exandriapodcast.R

class ShowSelectionAdapter(private val shows: List<String>) : RecyclerView.Adapter<ShowSelectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        private val showContainerView: ConstraintLayout

        init {
            titleTextView = view.findViewById(R.id.show_title)
            showContainerView = view.findViewById(R.id.show_container)
            showContainerView.setOnClickListener { Toast.makeText(view.context, "${titleTextView.text} Clicked", Toast.LENGTH_SHORT).show() }
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