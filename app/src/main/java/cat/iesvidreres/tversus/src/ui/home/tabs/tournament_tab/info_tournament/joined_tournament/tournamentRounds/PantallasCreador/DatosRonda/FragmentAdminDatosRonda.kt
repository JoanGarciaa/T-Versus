package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador.DatosRonda

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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentAdminDatosRondaBinding
import cat.iesvidreres.tversus.src.data.models.Equip
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador.AdminTournamentSharedViewModel
import com.app.projecte.tversus.AdaptadorMatch


class FragmentAdminDatosRonda : Fragment() {
    lateinit var binding: FragmentAdminDatosRondaBinding
    private val adminTournamentSharedViewModel: AdminTournamentSharedViewModel by activityViewModels()
    lateinit var launcher: ActivityResultLauncher<Intent>
    var posicion: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAdminDatosRondaBinding.inflate(inflater, container, false)

        var a = adminTournamentSharedViewModel.getRonda().value?.matches?:"Noop"

        var recyclerViewMatches = binding.recyclerviewMatches
        recyclerViewMatches.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        var adapter = AdaptadorMatch(emptyList())

        adapter.setOnItemClickListener(object : AdaptadorMatch.onItemClickListener{
            override fun onItemClick(position: Int) {

                posicion=position
                if (adminTournamentSharedViewModel.rondaActual.value?.matches!=null){
                    adminTournamentSharedViewModel.setMatch(adminTournamentSharedViewModel.rondaActual.value!!.matches.get(position))
                    val intent = Intent(requireContext(), DetallMatch::class.java).apply {
                        putExtra("match", adminTournamentSharedViewModel.rondaActual.value!!.matches.get(position))
                    }
                    launcher.launch(intent)
                }
            }
        })


        adapter.setTaskList(adminTournamentSharedViewModel.rondaActual.value?.matches)

        recyclerViewMatches.adapter=adapter

        adminTournamentSharedViewModel.rondaActual.observe(viewLifecycleOwner, Observer { datos ->
            adapter.setTaskList(adminTournamentSharedViewModel.rondaActual.value?.matches)
        })

        binding.buttonCerrarRonda.setOnClickListener(){

            //TODO Poner estado ronda en cerrado, crear la nueva ronda y luego sus match

            var matchesCerrados = 0
            for (match in adminTournamentSharedViewModel.rondaActual.value?.matches!!){
                if (!match.ganadorSeleccionado){
                    matchesCerrados++
                }
            }

            if (matchesCerrados==0){
                adminTournamentSharedViewModel.updateRound(
                    adminTournamentSharedViewModel.torneigActual.value!!.id,
                    adminTournamentSharedViewModel.rondaActual.value!!.roundNumber,
                    adminTournamentSharedViewModel.rondaActual.value!!
                ){ result ->
                    println(result)
                }
                //TODO Llevar al fragment anterior
                //view?.findNavController()?.navigate(R.id.action_fragmentAdminDatosRonda_to_adminRoundsTournamentFragment)


                adminTournamentSharedViewModel.afegirRonda()
            }else{
                Toast.makeText(requireContext(), "Selecciona todos los ganadores", Toast.LENGTH_SHORT).show()
                matchesCerrados=0
            }


        }
        if(adminTournamentSharedViewModel.rondaActual.value?.estadoRonda.equals("Cerrada")){
            binding.buttonCerrarRonda.setEnabled(false);
        }

        return binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val equipo = resultado.data?.getSerializableExtra("equipo") as? Equip
                adminTournamentSharedViewModel.rondaActual.value?.matches!!.get(posicion).ganador = equipo
                adminTournamentSharedViewModel.rondaActual.value?.matches!!.get(posicion).ganadorSeleccionado=true

            }
        }
    }
}