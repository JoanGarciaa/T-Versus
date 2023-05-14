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
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentInfoTournamentBinding
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.nodejs.userNode
import cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.list_tournaments_admin.info_tournament_admin.InfoTournamentAdminViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.JoinedTournamentRVAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class InfoTournamentFragment : Fragment() {
    private lateinit var binding: FragmentInfoTournamentBinding
    private val infoTournamentViewModel: InfoTournamentViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val infoTournamentAdminViewModel: InfoTournamentAdminViewModel by viewModels()
    val userLiveData = MutableLiveData<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoTournamentBinding.inflate(inflater, container, false)
        userNode.getUserFromNode(profileViewModel.authenticationRepository.getCurrentUser().email.toString()) { user ->
            userLiveData.postValue(user)
        }

        initUI()
        return binding.root
    }

    private fun initUI() {

        retrofit(infoTournamentViewModel.tournament!!)

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
                    userLiveData.observe(requireActivity()) { user ->
                        if (user.isJoined && tournament.id == user.tournament_id) {
                            binding.btnJoinTournament.isVisible = false
                            binding.btnToShowPlayers.isVisible = true
                            binding.btnToExitTournament.isVisible = true
                            binding.btnToShowPlayers.setOnClickListener {
                                view?.findNavController()
                                    ?.navigate(R.id.action_infoTournamentFragment_to_joinedTournamentFragment)
                            }
                            binding.btnToExitTournament.setOnClickListener {
                                userLiveData.observe(requireActivity()) { user ->
                                    val gson = GsonBuilder().setLenient().create()
                                    val retrofit =
                                        Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                                            .addConverterFactory(GsonConverterFactory.create(gson))
                                            .build()
                                    val finalTokens = tournament.price + user.tokens
                                    var new = User(
                                        user.username,
                                        user.email,
                                        user.password,
                                        user.borndate,
                                        finalTokens,
                                        "",
                                        user.image,
                                        false,
                                        user.points
                                    )
                                    val api = retrofit.create(userAPI::class.java);
                                    val builder = AlertDialog.Builder(requireContext())
                                    builder.setTitle("Cuidado!")
                                    builder.setMessage("Estas seguro de que quieres salir del torneo?")
                                    builder.setPositiveButton(
                                        "Si, salir!",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            try {
                                                api.updateUser(user.email, new)
                                                    .enqueue(object : Callback<User> {
                                                        override fun onResponse(
                                                            call: Call<User>,
                                                            responseUser: Response<User>
                                                        ) {
                                                            new = responseUser.body()!!
                                                        }

                                                        override fun onFailure(
                                                            call: Call<User>,
                                                            t: Throwable
                                                        ) {
                                                            Log.i("Error", "${t.cause}")
                                                            view?.findNavController()?.navigate(R.id.action_infoTournamentFragment_to_homeFragment)
                                                            Toast.makeText(context, "Has salido del torneo", Toast.LENGTH_SHORT).show()
                                                        }

                                                    })
                                            } catch (e: Error) {
                                                Log.e("Error en el catch", "$e");
                                            }
                                        })
                                    builder.setNegativeButton("No", null)
                                    val dialog = builder.create()
                                    dialog.show()

                                }
                            }
                        } else {
                            binding.btnJoinTournament.isVisible = true

                        }
                    }
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
                                    infoTournamentAdminViewModel.setUser(user)
                                    val oldTokens = user.tokens
                                    val finalTokens = user.tokens - infoTournament.price
                                    user.tokens = finalTokens
                                    user.tournament_id = infoTournament.id
                                    user.isJoined = true
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
                                        builder.setPositiveButton(
                                            "SI!",
                                            DialogInterface.OnClickListener { dialog, id ->
                                                apiUser.joinTournament(
                                                    profileViewModel.authenticationRepository.getCurrentUserEmail().email.toString(),
                                                    user
                                                ).enqueue(object : Callback<User> {
                                                    @SuppressLint("SetTextI18n")
                                                    override fun onResponse(
                                                        call: Call<User>, response: Response<User>
                                                    ) {
                                                        user.isJoined = true
                                                        user = response.body()!!
                                                    }

                                                    override fun onFailure(
                                                        call: Call<User>,
                                                        t: Throwable
                                                    ) {
                                                        Log.i("Error", "$t")
                                                    }
                                                })
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


    fun isUserJoined(userJoined: Boolean) {
        Log.i("userJoin", "$userJoined")
        if (userJoined) {
            binding.btnJoinTournament.visibility = View.GONE
            binding.btnToShowPlayers.visibility = View.VISIBLE
        }
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