package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasUser.DatosRonda

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentAdminDatosRondaBinding
import cat.iesvidreres.tversus.databinding.FragmentUserDatosRondaBinding
import cat.iesvidreres.tversus.src.data.models.Equip
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador.AdminTournamentSharedViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador.DatosRonda.DetallMatch
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasUser.UserTournamentSharedViewModel
import com.app.projecte.tversus.AdaptadorMatch


class FragmentUserDatosRonda : Fragment() {
    lateinit var binding: FragmentUserDatosRondaBinding
    private val userTournamentSharedViewModel: UserTournamentSharedViewModel by activityViewModels()
    lateinit var launcher: ActivityResultLauncher<Intent>
    var posicion: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUserDatosRondaBinding.inflate(inflater, container, false)

        var a = userTournamentSharedViewModel.getRonda().value?.matches?:"Noop"
        Log.d("Valor",a.toString())

        var recyclerViewMatches = binding.recyclerviewMatches
        recyclerViewMatches.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        var adapter = AdaptadorMatch(emptyList())

        adapter.setOnItemClickListener(object : AdaptadorMatch.onItemClickListener{
            override fun onItemClick(position: Int) {

                posicion=position
                if (userTournamentSharedViewModel.rondaActual.value?.matches!=null){
                    userTournamentSharedViewModel.setMatch(userTournamentSharedViewModel.rondaActual.value!!.matches.get(position))
                    val intent = Intent(requireContext(), DetallMatchUser::class.java).apply {
                        putExtra("match", userTournamentSharedViewModel.rondaActual.value!!.matches.get(position))
                    }
                    launcher.launch(intent)
                }
            }
        })


        adapter.setTaskList(userTournamentSharedViewModel.rondaActual.value?.matches)

        recyclerViewMatches.adapter=adapter

        userTournamentSharedViewModel.rondaActual.observe(viewLifecycleOwner, Observer { datos ->
            adapter.setTaskList(userTournamentSharedViewModel.rondaActual.value?.matches)
        })


        return binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val equipo = resultado.data?.getSerializableExtra("equipo") as? Equip
                userTournamentSharedViewModel.rondaActual.value?.matches!!.get(posicion).ganador = equipo
                userTournamentSharedViewModel.rondaActual.value?.matches!!.get(posicion).ganadorSeleccionado=true

            }
        }
    }
}