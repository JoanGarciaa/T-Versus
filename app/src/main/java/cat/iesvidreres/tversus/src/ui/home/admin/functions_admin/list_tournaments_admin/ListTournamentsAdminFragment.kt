package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.list_tournaments_admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentListTournamentsAdminBinding
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.InfoTournamentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListTournamentsAdminFragment : Fragment() {
    private lateinit var binding: FragmentListTournamentsAdminBinding
    private var cardAdapter = ListTournamentsRVAdapter()
    private val infoTournamentViewModel : InfoTournamentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentListTournamentsAdminBinding.inflate(inflater,container,false)
        initUI()
        return binding.root

    }

    private fun initUI(){
        setRecyclerView()
    }

    private fun setRecyclerView() {

        val cardRecyclerview = binding.rvTournamentsAdmin
        cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        cardRecyclerview.setHasFixedSize(true)
        cardRecyclerview.adapter = cardAdapter

        cardAdapter.setItemListener(object : ListTournamentsRVAdapter.OnItemClickListener {
            override fun onItemClick(tournament: Tournament) {
                infoTournamentViewModel.setTournament(tournament)
                view?.findNavController()?.navigate(R.id.action_listTournamentsAdminFragment_to_infoTournamentAdminFragment)
            }
        })
        observeCard()
    }

    private fun observeCard() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(tournamentAPI::class.java)
        var tournamentList: MutableList<Tournament>
        api.showTournaments().enqueue(object : Callback<MutableList<Tournament>> {
            override fun onResponse(
                call: Call<MutableList<Tournament>>, response: Response<MutableList<Tournament>>
            ) {

                tournamentList = response.body()!!
//                val tournaments = arrayOfNulls<String>(tournamentList.size)
//
//                for (i in tournamentList.indices) {
//                    tournaments[i] = tournamentList[i].name
//                }
                cardAdapter.setListData(tournamentList)
                cardAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<MutableList<Tournament>>, t: Throwable) {
                Log.i("Erroddr","$t")
            }


        })

    }

}