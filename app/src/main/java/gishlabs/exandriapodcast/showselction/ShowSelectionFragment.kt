package gishlabs.exandriapodcast.showselction

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import gishlabs.exandriapodcast.R
import gishlabs.exandriapodcast.data.Show
import gishlabs.exandriapodcast.databinding.FragmentShowSelectionBinding

class ShowSelectionFragment : Fragment(), ShowSelectionAdapter.OnShowSelectedListener {

    private var _binding: FragmentShowSelectionBinding? = null
    private val binding get() = _binding!!
    private var callbacks: Callbacks? = null

    interface Callbacks {
        // TODO Remove imageResouceID
        fun onShowSelected(sharedImage: ImageView, showTitle: String, imageResourceId: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowSelectionBinding.inflate(inflater, container, false)
        val view = binding.root
        initShowsGrid()

        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun initShowsGrid() {
        binding.showsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.showsRecyclerView.adapter = ShowSelectionAdapter(listOf(
            Show("Vox Machina", R.drawable.place_holder_image),
            Show("Mighty Nein", R.drawable.place_holder_image),
            Show("Bells Hells", R.drawable.place_holder_image),
            Show("Talks Machina", R.drawable.place_holder_image)
        ), this)

        binding.showsRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(6, 6, 6, 50)
            }
        })
    }

    override fun onShowSelected(sharedView: ImageView, title: String, imageResourceId: Int) {
        callbacks?.onShowSelected(sharedView, title, imageResourceId)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShowSelectionFragment()
    }


}