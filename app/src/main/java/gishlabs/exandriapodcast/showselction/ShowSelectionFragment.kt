package gishlabs.exandriapodcast.showselction

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gishlabs.exandriapodcast.databinding.FragmentShowSelectionBinding

class ShowSelectionFragment : Fragment() {

    private var _binding: FragmentShowSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowSelectionBinding.inflate(inflater, container, false)
        val view = binding.root
        initShowsGrid()

        return view
    }

    private fun initShowsGrid() {
        binding.showsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.showsRecyclerView.adapter = ShowSelectionAdapter(listOf(
            "Vox Machina",
            "Mighty Nein",
            "Bells Hells",
            "Talks Machina"
        ))

        binding.showsRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(6, 6, 6, 50)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShowSelectionFragment()
    }


}