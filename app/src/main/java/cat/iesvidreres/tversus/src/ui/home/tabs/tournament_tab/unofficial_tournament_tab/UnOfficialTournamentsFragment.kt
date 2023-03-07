package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.unofficial_tournament_tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentProfileBinding
import cat.iesvidreres.tversus.databinding.FragmentUnOfficialTournamentsBinding
import cat.iesvidreres.tversus.src.data.models.Tournament

class UnOfficialTournamentsFragment : Fragment() {
    private lateinit var binding: FragmentUnOfficialTournamentsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentUnOfficialTournamentsBinding.inflate(inflater,container,false)

        return binding.root
    }

}