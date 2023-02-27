package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentShopBinding
import cat.iesvidreres.tversus.databinding.FragmentTournamentBinding

class TournamentFragment : Fragment() {

    private lateinit var binding: FragmentTournamentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentTournamentBinding.inflate(inflater,container,false)

        return binding.root
    }


}