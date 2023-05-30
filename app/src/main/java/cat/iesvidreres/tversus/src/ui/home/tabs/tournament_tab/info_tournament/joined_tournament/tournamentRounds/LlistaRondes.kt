package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.Equip
import com.app.projecte.tversus.AdaptadorMatch

class LlistaRondes : AppCompatActivity() {
    var posicion: Int = 0
    private lateinit var launcher: ActivityResultLauncher<Intent>
    var viewModel: ViewModelRondas = ViewModelRondas()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_llista_rondes)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerMatches)
        viewModel.rondaActual?.crearMatches()
        var mAdaptader = AdaptadorMatch(viewModel.matchList)

        recyclerView.adapter = mAdaptader
        recyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
        viewModel.setRonda(0)

        for (Ronda in viewModel.rondaList) {
            Ronda.crearMatches()
        }


    }

    override fun onStart() {
        super.onStart()
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val equipo = resultado.data?.getSerializableExtra("equipo") as? Equip
                viewModel.matchList[posicion].ganador = equipo
            }
        }
    }
}