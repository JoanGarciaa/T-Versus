package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasUser

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.*
import cat.iesvidreres.tversus.src.data.providers.firebase.TournamentRepository
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserTournamentSharedViewModel : ViewModel() {
    var matchActual = MutableLiveData<Match>()
    var rondaActual = MutableLiveData<Ronda>()
    var posicionRonda: Int = 0
    var torneigActual = MutableLiveData<Tournament>()
    var repositorio: TournamentRepository = TournamentRepository()
    var rondasActuales = MutableLiveData<List<Ronda>>()


    fun setMatch(match: Match) {
        matchActual.value = match
    }

    fun getMatch(): MutableLiveData<Match> {
        return matchActual
    }

    fun setRonda(ronda: Ronda) {
        rondaActual.value = ronda
    }

    fun setPosicionRondaActual(ronda: Int) {
        posicionRonda = ronda
    }

    fun getRonda(): MutableLiveData<Ronda> {
        return rondaActual
    }

    fun setTorneig(torneig: Tournament) {
        torneigActual.value = torneig
    }

    fun getTorneig(): MutableLiveData<Tournament> {
        return torneigActual
    }

    fun getLlistaMatches(): List<Match>? {
        var matches = rondaActual.value?.matches
        return matches
    }


    fun getRondes() {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        val api = retrofit.create(tournamentAPI::class.java);
        Log.d("getrondes funciona", torneigActual.value?.id?:"Sin id")

        api.getRounds(torneigActual.value?.id ?: "0")
            .enqueue(object : Callback<List<Ronda>> {
                override fun onResponse(call: Call<List<Ronda>>, response: Response<List<Ronda>>) {
                    if (response.isSuccessful) {
                        val rounds = response.body()
                        Log.d("getrondes funciona y recibe estot", rounds.toString())
                        rondasActuales.value = rounds ?: emptyList()
                    } else {
                        Log.d("Rounds mal", "")
                    }
                }

                override fun onFailure(call: Call<List<Ronda>>, t: Throwable) {
                    Log.d("Rounds fallo", t.toString())
                }
            })
    }

    fun getUsersByTournamentId() {

        val userList = mutableListOf<Map<*, *>>()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/") // Reemplaza con tu dirección base de la API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(userAPI::class.java)
        val call = apiService.getUsersDataByTournamentId(torneigActual.value?.id?:"0")

        call.enqueue(object : Callback<List<Any>> {
            override fun onResponse(call: Call<List<Any>>, response: Response<List<Any>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    // Hacer algo con la lista de usuarios obtenida
                    if (users != null) {
                        for (user in users) {
                            if (user is Map<*, *>) { // Verificar si es un objeto Map
                                val username = user["username"] as String? // Acceder al campo "username"
                                val email = user["email"] as String? // Acceder al campo "email"

                                if (username != null && email != null) {
                                    // Hacer algo con los datos obtenidos
                                    println("Username: $username")
                                    println("Email: $email")
                                    userList.add(user)

                                }

                            }
                            torneigActual.value?.users = userList
                        }

                    }
                } else {
                    // Manejar el caso de respuesta no exitosa
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Any>>, t: Throwable) {
                // Manejar el caso de error de la llamada
                println("Error: ${t.message}")
            }
        })
    }

}
