package cat.iesvidreres.tversus.src.data.providers.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TournamentRepository {
    public fun getUnoficials(): LiveData<List<Tournament>> {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(tournamentAPI::class.java)

        var tournamentList: List<Tournament>

        var lista: MutableLiveData<List<Tournament>> = MutableLiveData<List<Tournament>>()

        api.getUnOfficialTournaments().enqueue(object : Callback<MutableList<Tournament>> {

            override fun onResponse(
                call: Call<MutableList<Tournament>>, response: Response<MutableList<Tournament>>
            ) {
                tournamentList = response.body()!!
                Log.i("hola","$tournamentList")
                lista.setValue(tournamentList)
            }

            override fun onFailure(call: Call<MutableList<Tournament>>, t: Throwable) {
                Log.i("Error","$t")
            }


        })
        return lista
    }
}