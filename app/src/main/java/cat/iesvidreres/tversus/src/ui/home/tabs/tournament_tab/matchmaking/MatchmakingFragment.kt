package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.matchmaking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentHomeBinding
import cat.iesvidreres.tversus.databinding.FragmentMatchmakingBinding
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.InfoTournamentViewModel
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class MatchmakingFragment : Fragment() {
    private lateinit var binding: FragmentMatchmakingBinding
    private val infoTournamentViewModel: InfoTournamentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentMatchmakingBinding.inflate(inflater,container,false)
        initUI()
        return binding.root
    }

    private fun initUI(){
    getUsers()
    }

    private fun getUsers() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(userAPI::class.java)
        var playerList: MutableList<User>
        var player = listOf<User>()
        val playerTextViews = listOf(
            binding.player0,binding.player1,binding.player2,binding.player3,binding.player4,binding.player5,binding.player6,binding.player7,binding.player8,binding.player9,
        )

        infoTournamentViewModel.tournament?.let { tournament ->
            api.showPlayers(tournament.id).enqueue(object : Callback<MutableList<User>> {
                override fun onResponse(
                    call: Call<MutableList<User>>, response: Response<MutableList<User>>
                ) {
                    try {
                        if (response.body() != null) {
                            playerList = response.body()!!
//                            for (player in playerList) {
//                                Log.i("jugadores","${player.username}")
//                            }
                            for ((index, player) in playerList.withIndex()) {
                                playerTextViews[index].text = player.username
                            }

                        } else {
                            toast("no hay jugadores dentro de este torneo")
                        }
                    } catch (e: ApiException) {
                        Log.i("Error in players call", "$e")
                    }
                }

                override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                    Log.i("Erroddr", "$t")
                }


            })
        }

    }

}