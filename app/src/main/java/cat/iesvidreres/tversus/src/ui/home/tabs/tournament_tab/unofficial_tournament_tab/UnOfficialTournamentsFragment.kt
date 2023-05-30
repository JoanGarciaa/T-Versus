package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.unofficial_tournament_tab

import android.annotation.SuppressLint
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
import cat.iesvidreres.tversus.databinding.FragmentUnOfficialTournamentsBinding
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.InfoTournamentViewModel
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class UnOfficialTournamentsFragment : Fragment() {
    private lateinit var binding: FragmentUnOfficialTournamentsBinding
    private var cardAdapter = UnOfficialTournamentsRVAdapter()
    private val infoTournamentViewModel : InfoTournamentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentUnOfficialTournamentsBinding.inflate(inflater,container,false)
        initUI()
        return binding.root
    }

    private fun initUI(){
        setRecyclerView()
    }

    private fun setRecyclerView() {

        val cardRecyclerview = binding.rvUnOfficialTournaments
        cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        cardRecyclerview.setHasFixedSize(true)
        cardRecyclerview.adapter = cardAdapter

        cardAdapter.setItemListener(object : UnOfficialTournamentsRVAdapter.OnItemClickListener {
            override fun onItemClick(tournament: Tournament) {
                infoTournamentViewModel.setTournament(tournament)
                view?.findNavController()?.navigate(R.id.action_unOfficialTournamentsFragment_to_infoTournamentFragment)
            }
        })
        observeCard()
    }

    private fun observeCard() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(tournamentAPI::class.java)
        var tournamentList: MutableList<Tournament>
        api.getTournamentsTipus("unofficial").enqueue(object : Callback<MutableList<Tournament>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<MutableList<Tournament>>, response: Response<MutableList<Tournament>>
            ) {
                try {
                    tournamentList = response.body()!!
                    cardAdapter.setListData(tournamentList)
                    cardAdapter.notifyDataSetChanged()
                } catch (e: NullPointerException) {
                    toast("No hay torneos disponibles!")
                }
            }

            override fun onFailure(call: Call<MutableList<Tournament>>, t: Throwable) {
                if (t is HttpException && t.code() == 404) {
                    Log.e("Error", "No se encontraron torneos")
                } else {
                    Log.e("Error", "$t")
                }
            }
        })

    }

}