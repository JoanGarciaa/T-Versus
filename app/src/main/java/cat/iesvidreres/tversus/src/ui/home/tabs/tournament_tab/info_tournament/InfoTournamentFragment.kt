package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentInfoTournamentBinding
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InfoTournamentFragment : Fragment() {
    private lateinit var binding: FragmentInfoTournamentBinding
    private val infoTournamentViewModel: InfoTournamentViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var isJoined : Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoTournamentBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        retrofit(infoTournamentViewModel.tournament!!)
        showPlayers()
    }

    fun showPlayers(){
        if(isJoined){
            binding.btnJoinTournament.isVisible = false
            binding.btnToShowPlayers.isVisible = true
            binding.btnToShowPlayers.setOnClickListener{
                view?.findNavController()?.navigate(R.id.action_infoTournamentFragment_to_joinedTournamentFragment)
            }
        }else{
            binding.btnJoinTournament.isVisible = true
        }
    }

    fun retrofit(tournament: Tournament) {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val apiTournament = retrofit.create(tournamentAPI::class.java)
        var infoTournament: Tournament
        apiTournament.getTournamentID(tournament.id).enqueue(object : Callback<Tournament> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<Tournament>, response: Response<Tournament>
            ) {

                infoTournament = response.body()!!
                with(binding) {
                    tvInfoTournamentName.text = infoTournament.name
                    tvInfoTournamentDescription.text = "Descripcion: " + infoTournament.description
                    tvInfoTournamentGame.text = "GAME: " + infoTournament.game
                    definePrice(infoTournament.price)
                    progressBarInfoTournament.visibility = View.GONE
                    btnJoinTournament.setOnClickListener {
                        val apiUser = retrofit.create(userAPI::class.java)
                        var user: User
                        apiUser.getUserByEmail(profileViewModel.authenticationRepository.getCurrentUserEmail().email.toString())
                            .enqueue(object : Callback<User> {
                                @SuppressLint("SetTextI18n")
                                override fun onResponse(
                                    call: Call<User>, response: Response<User>
                                ) {
                                    user = response.body()!!
                                    val oldTokens = user.tokens
                                    val finalTokens = user.tokens - infoTournament.price
                                    user.tokens = finalTokens
                                    user.tournament_id = infoTournament.id

                                    if (oldTokens < infoTournament.price) {
                                        Toast.makeText(
                                            requireContext(),
                                            "No tienes dinero para acceder a este torneo",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        showDialogComprar()
                                    } else {
                                        val builder = AlertDialog.Builder(requireContext())
                                        builder.setTitle("Te vas a unir!")
                                        builder.setMessage("Estas seguro de que quieres unirte al torneo?")
                                        builder.setPositiveButton("SI!", DialogInterface.OnClickListener { dialog, id ->
                                            apiUser.joinTournament(
                                                profileViewModel.authenticationRepository.getCurrentUserEmail().email.toString(),
                                                user
                                            ).enqueue(object : Callback<User> {
                                                @SuppressLint("SetTextI18n")
                                                override fun onResponse(
                                                    call: Call<User>, response: Response<User>
                                                ) {
                                                    user = response.body()!!
                                                }

                                                override fun onFailure(call: Call<User>, t: Throwable) {
                                                    Log.i("Error", "$t")
                                                }
                                            })
                                            isJoined = true
                                            view?.findNavController()?.navigate(R.id.action_infoTournamentFragment_to_joinedTournamentFragment)
                                        })
                                        builder.setNegativeButton("Aun no", null)
                                        val dialog = builder.create()
                                        dialog.show()
                                    }
                                }

                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    Log.i("Error", "$t")
                                }

                            })
                    }
                }
            }

            override fun onFailure(call: Call<Tournament>, t: Throwable) {
                Log.i("Error", "$t")
            }

        })

    }

    fun showDialogComprar() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("¡Vaya!")
        builder.setMessage("Parece que no tienes mas tokens y necesitas comprar")
        builder.setPositiveButton("Comprar", DialogInterface.OnClickListener { dialog, id ->
            view?.findNavController()?.navigate(
                R.id.action_infoTournamentFragment_to_shopFragment
            )
        })
        builder.setNegativeButton("Ahora no", null)
        val dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun definePrice(price: Int) {
        if (price == 0) {
            binding.tvInfoTournamentPrice.text = "¡Gratis!"
        } else {
            binding.tvInfoTournamentPrice.text = "Precio: $price"
        }
    }


}