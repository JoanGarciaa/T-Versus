package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.matchmaking

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentHomeBinding
import cat.iesvidreres.tversus.databinding.FragmentMatchmakingBinding
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.nodejs.userNode
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.InfoTournamentViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.report_user.ReportViewModel
import com.google.android.gms.common.api.ApiException
import com.squareup.picasso.Picasso
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
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val reportViewModel: ReportViewModel by activityViewModels()

    val userLiveData = MutableLiveData<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMatchmakingBinding.inflate(inflater, container, false)
        initUI()
        userNode.getUserFromNode(profileViewModel.authenticationRepository.getCurrentUser().email.toString()) { user ->
            userLiveData.postValue(user)
            binding.btnToReport.setOnClickListener {
                userNode.getUserFromNode(profileViewModel.authenticationRepository.getCurrentUser().email.toString()) { user ->
                    //SVM
                    reportViewModel.setInfoReport(user.username,user.tournament_id)
                }
                //bundle
                view?.findNavController()?.navigate(R.id.action_matchmakingFragment_to_reportFragment, bundleOf("username" to user.username,"idTournament" to user.tournament_id ))

            }
        }
        return binding.root
    }

    private fun initUI() {
        navigation(bundleOf())
        getUsers()
        initEditTexts()
    }

    fun initEditTexts() {
        with(binding) {
            val editTexts = listOf(
                result0,
                result1,
                result2,
                result3,
                result4,
                result5,
                result6,
                result7,
                result8,
                result9,
            )
            editTexts.forEach { it.isEnabled = false }
        }
    }

    private fun getUsers() {
        var selectedEditText: EditText? = null

        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(userAPI::class.java)
        var playerList: MutableList<User>

        with(binding) {
            val playerTextViews = listOf(
                player0,
                player1,
                player2,
                player3,
                player4,
                player5,
                player6,
                player7,
                player8,
                player9
            )
            infoTournamentViewModel.tournament?.let { tournament ->
                api.showPlayers(tournament.id).enqueue(object : Callback<MutableList<User>> {
                    override fun onResponse(
                        call: Call<MutableList<User>>, response: Response<MutableList<User>>
                    ) {
                        try {
                            if (response.body() != null) {
//                                playerList = response.body()!!
//                                for ((index, player) in playerList.withIndex()) {
//                                    playerTextViews[index].text = player.username
//                                }
                                playerList = response.body()!!

                                for ((index, player) in playerList.withIndex()) {
                                    val playerTextView = playerTextViews[index]
                                    playerTextView.text = player.username
                                    val playerPoints = player.points.toString()

                                    selectedEditText?.text = Editable.Factory.getInstance().newEditable(player.points.toString())
                                    val resultEditText = when (playerTextView) {
                                        player0 -> result0
                                        player1 -> result1
                                        player2 -> result2
                                        player3 -> result3
                                        player4 -> result4
                                        player5 -> result5
                                        player6 -> result6
                                        player7 -> result7
                                        player8 -> result8
                                        player9 -> result9
                                        else -> null
                                    }
                                    playerTextViews.forEachIndexed { index, playerTextView ->
                                        if(playerPoints != "0"){
                                            resultEditText?.text = Editable.Factory.getInstance().newEditable(playerPoints)
                                        }
                                    }


                                        playerTextView.setOnClickListener {
                                        // Deshabilitar EditText anteriormente seleccionado, si lo hay
                                        userLiveData.observe(requireActivity()) { user ->
                                            if (user.username == playerTextView.text) {
                                                selectedEditText?.isEnabled = false
                                                // Habilitar el EditText correspondiente
                                                resultEditText?.isEnabled = true
                                                // Guardar el EditText seleccionado
                                                selectedEditText = resultEditText
                                                binding.btnSavePoints.setOnClickListener {
                                                    if (resultEditText != null) {
                                                        val userPoints = User(
                                                            user.username,
                                                            user.email,
                                                            user.password,
                                                            user.borndate,
                                                            user.tokens,
                                                            user.tournament_id,
                                                            user.image,
                                                            user.isJoined,
                                                            resultEditText.text.toString().toInt()
                                                        )
                                                        api.updateUser(user.email, userPoints).enqueue(object : Callback<User> {
                                                                override fun onResponse(call: Call<User>, response: Response<User>) {
                                                                }

                                                                @SuppressLint("LogNotTimber")
                                                                override fun onFailure(call: Call<User>, t: Throwable) {
                                                                    Log.e("Error","$t")
                                                                }
                                                            })
                                                        view?.findNavController()?.navigate(R.id.action_matchmakingFragment_to_joinedTournamentFragment)
                                                        toast("Gracias por poner tu puntuación!")

                                                    }
                                                }
                                            } else {
                                                resultEditText?.isEnabled = false
                                                selectedEditText?.isEnabled = false
                                            }
                                        }

                                    }


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
    fun navigation(bundle: Bundle){

    }

}