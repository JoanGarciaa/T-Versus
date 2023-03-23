package cat.iesvidreres.tversus.src.ui.home.tabs.home_tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.databinding.FragmentHomeBinding
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var cardAdapter = HomeRVAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentHomeBinding.inflate(inflater,container,false)
        initUI()
        return binding.root
    }

    private fun initUI(){
        setRecyclerView()
    }

    private fun setRecyclerView() {

        val cardRecyclerview = binding.rvViewTournaments
        cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        cardRecyclerview.setHasFixedSize(true)
        cardRecyclerview.adapter = cardAdapter

        cardAdapter.setItemListener(object : HomeRVAdapter.OnItemClickListener {
            override fun onItemClick(tournament: Tournament) {
                val tournament = tournament.id
                Log.i("addasd","$tournament")
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
            }

            override fun onFailure(call: Call<MutableList<Tournament>>, t: Throwable) {
                Log.i("Erroddr","$t")
            }


        })

    }

}