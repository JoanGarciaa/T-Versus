package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentCreateTournamentBinding
import cat.iesvidreres.tversus.databinding.FragmentInfoTournamentBinding
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InfoTournamentFragment : Fragment() {
    private lateinit var binding: FragmentInfoTournamentBinding
    private val infoTournamentViewModel: InfoTournamentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoTournamentBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI(){
        retrofit(infoTournamentViewModel.tournament!!)
    }

    fun retrofit(tournament: Tournament){
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(tournamentAPI::class.java)
        var infoTournament : Tournament
        api.getTournamentID(tournament.id).enqueue(object : Callback<Tournament> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<Tournament>, response: Response<Tournament>
            ) {
                infoTournament = response.body()!!
                with(binding){
                    tvInfoTournamentName.text = infoTournament.name
                    tvInfoTournamentDescription.text = "Descripcion: " + infoTournament.description
                    tvInfoTournamentGame.text = "GAME: " + infoTournament.game
                }

            }

            override fun onFailure(call: Call<Tournament>, t: Throwable) {
                Log.i("Erroddr","$t")
            }

        })

    }



}