package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador

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
import cat.iesvidreres.tversus.databinding.FragmentRoundsTournamentBinding


class AdminRoundsTournamentFragment : Fragment() {

    lateinit var binding: FragmentRoundsTournamentBinding
    private val adminTournamentSharedViewModel: AdminTournamentSharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoundsTournamentBinding.inflate(inflater, container, false)

        val recyclerViewRondas = binding.roundsRecyclerView
        val adaptador = AdminRoundsAdapter()

        if (adminTournamentSharedViewModel.torneigActual.value?.rounds!=null){

            Log.d("Lista",adminTournamentSharedViewModel.torneigActual.value!!.rounds.toString()?:"A")
        }else{
            Log.d("Lista","Esta vacia")
           // adminTournamentSharedViewModel.FirstRound()
            adminTournamentSharedViewModel.getRondes()
            Log.d("Torneo",adminTournamentSharedViewModel.torneigActual.value?.rounds.toString())
        }

        adminTournamentSharedViewModel.torneigActual.value?.let { adaptador.setListData(adminTournamentSharedViewModel.torneigActual.value?.rounds?: emptyList()) }
        //adminTournamentSharedViewModel.FirstRound()
        //adminTournamentSharedViewModel.afegirRonda()
        adminTournamentSharedViewModel.getUsersByTournamentId()

        adaptador.setOnItemClickListener(object : AdminRoundsAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Log.d("position",position.toString()+ adaptador.get(position))
                adminTournamentSharedViewModel.setRonda(adaptador.get(position))
                adminTournamentSharedViewModel.posicionRonda=position

                var a = adminTournamentSharedViewModel.rondaActual.value?.matches?:"Noop"
                Log.d("Valor",a.toString())
                view?.findNavController()?.navigate(R.id.action_adminRoundsTournamentFragment_to_fragmentAdminDatosRonda)

            }



        })
        recyclerViewRondas.adapter=adaptador

        recyclerViewRondas.layoutManager = LinearLayoutManager(requireContext())

        adminTournamentSharedViewModel.rondasActuales.observe(viewLifecycleOwner, Observer { ronda ->
            adaptador.setListData(ronda)
            Log.d("Lista nuevos valores",ronda.toString())
        })




        return binding.root
    }


}