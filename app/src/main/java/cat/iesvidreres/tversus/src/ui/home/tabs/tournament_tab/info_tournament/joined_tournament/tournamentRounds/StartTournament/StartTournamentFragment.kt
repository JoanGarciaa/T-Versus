package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.StartTournament

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.JoinedTournamentRVAdapter
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.databinding.FragmentJoinedTournamentBinding
import cat.iesvidreres.tversus.databinding.FragmentStartTournamentBinding
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.InfoTournamentViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.LlistaMatches
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.LlistaRondes
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador.AdminTournamentSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class JoinedTournamentFragment : Fragment() {
    private lateinit var binding: FragmentStartTournamentBinding
    private var cardAdapter = JoinedTournamentRVAdapter()
    private val infoTournamentViewModel: InfoTournamentViewModel by activityViewModels()
    private val adminTournamentSharedViewModel: AdminTournamentSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartTournamentBinding.inflate(inflater, container, false)

        initUI()

        return binding.root
    }

    private fun initUI() {
        goToRounds()
        setRecyclerView()
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
        observeCard()
    }

    private fun observeCard() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(userAPI::class.java)
        var playerList: MutableList<User>
        infoTournamentViewModel.tournament?.let {
            api.showPlayers(it.id).enqueue(object : Callback<MutableList<User>> {
                override fun onResponse(
                    call: Call<MutableList<User>>, response: Response<MutableList<User>>
                ) {
                    try {
                        playerList = response.body()!!
                        cardAdapter.setListData(playerList)
                        cardAdapter.notifyDataSetChanged()
                    } catch (e: NullPointerException) {
                        toast("No hay jugadores disponibles!")
                    }
                }

                override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                    Log.i("Erroddr", "$t")
                }
            })
        }

    }

    private fun goToRounds(){
        binding.btnToRounds.setOnClickListener {

            MySingleton.myTournament?.actualRound ?:    0
           var intent = Intent(requireContext(), LlistaRondes::class.java)
            startActivity(intent)

        }

        binding.buttonAbrirTorneo.setOnClickListener(){

        }
    }


}

object MySingleton {
    var myTournament: Tournament? = null
}