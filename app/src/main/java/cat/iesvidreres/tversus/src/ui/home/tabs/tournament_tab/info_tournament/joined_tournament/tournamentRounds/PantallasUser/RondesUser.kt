package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasUser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentRondesUserBinding
import cat.iesvidreres.tversus.databinding.FragmentRoundsTournamentBinding
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador.AdminRoundsAdapter
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador.AdminTournamentSharedViewModel


class RondesUser : Fragment() {

    lateinit var binding: FragmentRondesUserBinding
    private val userTournamentSharedViewModel: UserTournamentSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRondesUserBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val recyclerViewRondas = binding.recyclerRondasUser
        val adaptador = AdminRoundsAdapter()
        recyclerViewRondas.adapter=adaptador

        recyclerViewRondas.layoutManager = LinearLayoutManager(requireContext())
        Log.d("Torneo RondesUser 36",userTournamentSharedViewModel.torneigActual.value.toString())

        Log.d("Tournament", userTournamentSharedViewModel.torneigActual.value.toString())
        if (userTournamentSharedViewModel.torneigActual.value?.rounds!=null){
            userTournamentSharedViewModel.torneigActual.value?.rounds?.let { adaptador.setListData(it) }
            Log.d("Lista",userTournamentSharedViewModel.torneigActual.value!!.rounds.toString()?:"A")
        }else{
            Log.d("Lista","Esta vacia")
            userTournamentSharedViewModel.getRondes()
            Log.d("Torneo rondas",userTournamentSharedViewModel.torneigActual.value?.rounds.toString())
        }

        adaptador.setOnItemClickListener(object : AdminRoundsAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Log.d("position",position.toString()+ adaptador.get(position))
                userTournamentSharedViewModel.setRonda(adaptador.get(position))
                userTournamentSharedViewModel.posicionRonda=position

                var a = userTournamentSharedViewModel.rondaActual.value?.matches?:"Noop"
                Log.d("Valor",a.toString())
                view?.findNavController()?.navigate(R.id.fragmentUserDatosRonda)

            }})

       userTournamentSharedViewModel.rondasActuales.observe(viewLifecycleOwner, Observer { ronda ->
            adaptador.setListData(ronda)
            Log.d("Lista nuevos valores",ronda.toString())
        })

        return binding.root
    }
}