package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.Equip
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador.DatosRonda.DetallMatch
import com.app.projecte.tversus.AdaptadorMatch

class LlistaMatches : AppCompatActivity() {
    var posicion: Int =0
    private lateinit var launcher: ActivityResultLauncher<Intent>
    var viewModel: ViewModelRondas = ViewModelRondas()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_llista_matches)



        var recyclerView = findViewById<RecyclerView>(R.id.recyclerMatches)
        viewModel.rondaActual?.crearMatches()
        var mAdaptader = AdaptadorMatch(viewModel.matchList)

        recyclerView.adapter= mAdaptader
        recyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
        viewModel.setRonda(0)

        for (Ronda in viewModel.rondaList){
            Ronda.crearMatches()
        }

        mAdaptader.setOnItemClickListener(object : AdaptadorMatch.onItemClickListener{
            override fun onItemClick(position: Int) {
                viewModel.setMatch(position)
                posicion=position
                val intent = Intent(applicationContext, DetallMatch::class.java).apply {
                    putExtra("match", viewModel.matchList[position])
                }
                Log.d("Ganador",viewModel.matchList[position].ganador.toString())
                launcher.launch(intent)
            }
        })

        var boton:Button = findViewById(R.id.buttonCerrarRondaDeprecado)
        boton.setOnClickListener(){
            var todosTienenGanador = true
            for (match in viewModel.matchList) {
                if (match.ganador == null) {
                    todosTienenGanador = false
                    break
                }
            }
            if (todosTienenGanador) {
                Log.d("TagGanador","Todos los match están cerrados")
                //TODO Funcion que guarde los datos de l ronda
            }else{
                Log.d("TagGanador","Alguna no tiene")
            }
        }
    }
    override fun onStart() {
        super.onStart()
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val equipo = resultado.data?.getSerializableExtra("equipo") as? Equip
                val bool = resultado.data?.getBooleanExtra("ganador", false)
                viewModel.matchList[posicion].ganador=equipo
                if (bool != null) {
                    viewModel.matchList[posicion].ganadorSeleccionado=bool
                }
            }
        }


    }}