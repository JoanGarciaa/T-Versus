package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentAdminStartTournamentBinding
import cat.iesvidreres.tversus.databinding.FragmentRoundsTournamentBinding
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.JoinedTournamentRVAdapter


class AdminStartTournament : Fragment() {


    lateinit var binding: FragmentAdminStartTournamentBinding
    private val adminTournamentSharedViewModel: AdminTournamentSharedViewModel by activityViewModels()
    private var cardAdapter = JoinedTournamentRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminStartTournamentBinding.inflate(inflater, container, false)
        adminTournamentSharedViewModel.getUsersByTournamentId()
        binding.buttonAbrirTorneo.setOnClickListener(){
            adminTournamentSharedViewModel.FirstRound()
            view?.findNavController()?.navigate(R.id.action_adminStartTournament_to_joinedTournamentFragment)
        }

        binding.btnToRounds.setOnClickListener(){
            view?.findNavController()?.navigate(R.id.action_adminStartTournament_to_adminRoundsTournamentFragment)
        }
        return binding.root
    }


    private fun setRecyclerView() {

        val cardRecyclerview = binding.rvPlayers
        cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        cardRecyclerview.setHasFixedSize(true)
        cardRecyclerview.adapter = cardAdapter

        cardAdapter.setItemListener(object : JoinedTournamentRVAdapter.OnItemClickListener {
            override fun onItemClick(user: User) {
            }
        })
    }
}