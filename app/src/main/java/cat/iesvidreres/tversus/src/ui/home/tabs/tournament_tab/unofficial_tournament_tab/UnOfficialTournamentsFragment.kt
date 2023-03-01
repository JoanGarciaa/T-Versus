package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.unofficial_tournament_tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentUnOfficialTournamentsBinding
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.unofficial_tournament_tab.recyclerviewNoOficiales.NoOficialesAdapter

class UnOfficialTournamentsFragment : Fragment() {
    private lateinit var adapter: NoOficialesAdapter
    private lateinit var binding: FragmentUnOfficialTournamentsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val tournaments: MutableLiveData<List<Tournament>?> = MutableLiveData()
        adapter = NoOficialesAdapter(tournaments.value.orEmpty())

        binding = FragmentUnOfficialTournamentsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerViewNoOficiales.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNoOficiales.adapter = adapter
        return binding.root
    }

}