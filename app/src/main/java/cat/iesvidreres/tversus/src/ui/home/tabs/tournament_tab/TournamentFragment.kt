package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentTournamentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TournamentFragment : Fragment() {

    private lateinit var binding: FragmentTournamentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentTournamentBinding.inflate(inflater,container,false)
        chooseTournament()

        return binding.root
    }

    fun chooseTournament(){
        binding.btnOficial.setOnClickListener(){
            view?.findNavController()?.navigate(R.id.action_tournamentFragment_to_officialTournamentFragment)
        }

        binding.btnNoOfficial.setOnClickListener(){
            view?.findNavController()?.navigate(R.id.action_tournamentFragment_to_unOfficialTournamentsFragment)

        }

        binding.btnToCreateTournament.setOnClickListener{
            view?.findNavController()?.navigate(R.id.action_tournamentFragment_to_createTournamentFragment)
        }


    }


}