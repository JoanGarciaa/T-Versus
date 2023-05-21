package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.list_tournaments_admin.info_tournament_admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentInfoTournamentAdminBinding
import cat.iesvidreres.tversus.databinding.FragmentListTournamentsAdminBinding
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.InfoTournamentViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.JoinedTournamentRVAdapter
import com.google.android.gms.common.api.ApiException
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class InfoTournamentAdminFragment : Fragment() {
    private lateinit var binding: FragmentInfoTournamentAdminBinding
    private val infoTournamentViewModel: InfoTournamentViewModel by activityViewModels()
    private var cardAdapter = InfoTournamentAdminRVAdapter()
    private val infoTournamentAdminViewModel : InfoTournamentAdminViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoTournamentAdminBinding.inflate(inflater, container, false)
        initUI()

        binding.btnToResults.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_infoTournamentAdminFragment_to_matchmakingFragment)
        }
        binding.btnShowReports.setOnClickListener { 
            toast("Futura implementación")
        }
        return binding.root
    }

    private fun initUI() {
        retrofit(infoTournamentViewModel.tournament!!)
        setRecyclerView()
    }

    private fun setRecyclerView() {

        val cardRecyclerview = binding.rvInfoTournamentPlayers
        cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        cardRecyclerview.setHasFixedSize(true)
        cardRecyclerview.adapter = cardAdapter

        cardAdapter.setItemListener(object : InfoTournamentAdminRVAdapter.OnItemClickListener {
            override fun onItemClick(user: User) {
            }

            override fun onDeleteUser(user: User) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Cuidado!")
                builder.setMessage("Estas seguro de que quieres sacar del torneo al usuario?")
                builder.setPositiveButton(
                    "Si, sacar!",
                    DialogInterface.OnClickListener { dialog, id ->
                        cardAdapter.notifyDataSetChanged()
                        val gson = GsonBuilder().setLenient().create()
                        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                            .addConverterFactory(GsonConverterFactory.create(gson)).build()
                        val api = retrofit.create(userAPI::class.java)
                        var thisUser = User(user.username,user.email,user.password,user.borndate,user.tokens,"",user.image,user.isJoined,0)
                        api.updateUser(user.email,thisUser).enqueue(object : Callback<User> {
                            override fun onResponse(
                                call: Call<User>, response: Response<User>
                            ) {
                                thisUser = response.body()!!
                                cardAdapter.notifyDataSetChanged()
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                cardAdapter.notifyDataSetChanged()

                            }
                        })
                    })
                builder.setNegativeButton("No", null)
                val dialog = builder.create()
                dialog.show()
            }
        })
        observeCard()
    }

    fun retrofit(tournament: Tournament) {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val apiTournament = retrofit.create(tournamentAPI::class.java)
        var infoTournament: Tournament
        apiTournament.getTournamentID(tournament.id).enqueue(object : Callback<Tournament> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<Tournament>, response: Response<Tournament>
            ) {

                infoTournament = response.body()!!
                with(binding) {
                    idInfoTournament.text = "ID: " + infoTournament.id
                    nameInfoTournament.text = "Nombre: " + infoTournament.description
                    descriptionInfoTournament.text = "Descripcion: " + infoTournament.game
                    priceInfoTournament.text = infoTournament.price.toString()
                    gameInfoTournament.text = infoTournament.game
                    typeInfoTournament.text = infoTournament.type
                    organizerInfoTournament.text = infoTournament.organizer

                    val apiUser = retrofit.create(userAPI::class.java)
                    var tournamentDeleted = tournament
                    ivDeleteTournament.setOnClickListener{
                        apiTournament.deleteTournament(infoTournament.id).enqueue(object : Callback<Tournament> {
                            override fun onResponse(call: Call<Tournament>, response: Response<Tournament>) {
                                tournamentDeleted = response.body()!!
                                toast("Torneo eliminado!")
                            }

                            override fun onFailure(call: Call<Tournament>, t: Throwable) {
                                Log.i("No se ha podido borrar el torneo" ,"$t")
                                toast("Torneo eliminado!")
                                view?.findNavController()?.navigate(R.id.action_infoTournamentAdminFragment_to_functionsAdminFragment)

                            }

                        })
                    }
                }
            }

            override fun onFailure(call: Call<Tournament>, t: Throwable) {
                Log.i("Erroddr", "$t")
            }

        })

    }

    private fun observeCard() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(userAPI::class.java)
        var playerList: MutableList<User>
        infoTournamentViewModel.tournament?.let { tournament ->
            api.showPlayers(tournament.id).enqueue(object : Callback<MutableList<User>> {
                override fun onResponse(
                    call: Call<MutableList<User>>, response: Response<MutableList<User>>
                ) {
                    try {
                        if (response.body() != null) {
                            playerList = response.body()!!
                            cardAdapter.setListData(playerList)
                        } else {
                            toast("no hay jugadores dentro de este torneo")
                        }
                    } catch (e: ApiException) {
                        Log.i("Error in players call", "$e")
                    }
                    cardAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                    Log.i("Erroddr", "$t")
                }


            })
        }

    }


}