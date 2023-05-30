package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.VERSION_CODES.M
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.core.Event
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.*
import cat.iesvidreres.tversus.src.data.providers.firebase.TournamentRepository
import cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab.payment.model.Payment
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.StartTournament.MySingleton
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.stream.Collectors.toList
import javax.inject.Inject

class AdminTournamentSharedViewModel : ViewModel() {

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

    fun FirstRound() {
        var limite = (torneigActual.value?.teamsNumber?.times(5))?:40
        var lista = torneigActual.value?.users
        var numero = 1
        var ronda: Ronda = Ronda(
            numero,
            torneigActual.value?.id ?: "1",
            crearEquipos(lista ?: emptyList(),limite),
            "Abierta",
            emptyList()
        )

        ronda.crearMatches()
        deleteRound()

        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        val api = retrofit.create(tournamentAPI::class.java);
        api.addRound(torneigActual.value!!.id, ronda).enqueue(object : Callback<Ronda> {

            override fun onResponse(call: Call<Ronda>, response: Response<Ronda>) {

                Log.d("Correcto", "Torneo actualizado" + response)
                torneigActual.value?.actualRound = ronda.roundNumber
                actualizarRondaActual()

            }

            override fun onFailure(call: Call<Ronda>, t: Throwable) {
                Log.d("InCorrecto", "Torneo no actualizado " + t)
            }

        })
    }

    fun crearEquipos(users: List<Map<*,*>>,limite:Int): MutableList<Equip> {

        val numUsers = torneigActual.value?.users?.size ?:0
        Log.d("Users",torneigActual.value?.users.toString())

        val nombres = listOf(
            "Ana", "Beto", "Carla", "Daniel", "Elena", "Fernando", "Gabriela", "Hugo",
            "Inés", "Juan", "Karla", "Luis", "María", "Nicolás", "Olivia", "Pedro",
            "Quim", "Rosa", "Santiago", "Teresa", "Ursula", "Víctor", "Wendy", "Xavier",
            "Yolanda", "Zacarías", "Alejandra", "Benito", "Catalina", "Diego", "Emilia", "Felipe",
            "Gloria", "Héctor", "Isabela", "Javier", "Karen", "Lorenzo", "Mónica"
        )

        //TODO Hacer una lista de los usuarios que hay e ir añadiendole generados hasta el limite
        var j = 0
        var listaUsers: MutableList<Map<*, *>> = mutableListOf()
        listaUsers.addAll(torneigActual.value?.users ?: emptyList())

        while (listaUsers.size<limite){
            val map: MutableMap<String, Any> = mutableMapOf()
            var nombre = nombres[j]

            map["username"] = nombre
            map["correu"] = nombre+"@institutvidreres,cat"
            listaUsers.add(map)
            j++
            if (j==nombres.size){
                j=0
            }
        }

        val result: List<List<Map<*,*>>> = splitParticipantsIntoGroups(listaUsers)
        var equips: MutableList<Equip> = mutableListOf()
        var participantes: MutableList<Equip> = mutableListOf()

        var numeroParticipantes= torneigActual.value?.users?.size?:0

        var i = 0
        for (lista in result) {
            i++
            val equipo = Equip("Equipo $i", lista)
            equips.add(equipo)

        }

        return equips
    }

    fun splitParticipantsIntoGroups(participants: List<Map<*, *>>): List<List<Map<*, *>>> {
        val groups = mutableListOf<List<Map<*, *>>>()
        var currentGroup = mutableListOf<Map<*, *>>()

        for (participant in participants) {
            currentGroup.add(participant)

            if (currentGroup.size == 5) {
                groups.add(currentGroup)
                currentGroup = mutableListOf<Map<*, *>>()
            }
        }

        // Añadir el último grupo incompleto si existe
        if (currentGroup.isNotEmpty()) {
            groups.add(currentGroup)
        }

        return groups
    }

    fun actualitzarTorneig() {
        if (torneigActual != null) {

            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build()

            val api = retrofit.create(tournamentAPI::class.java);
            api.updateTournament(torneigActual.value!!.id, torneigActual.value!!)
                .enqueue(object : Callback<Tournament> {
                    override fun onResponse(
                        call: Call<Tournament>,
                        response: Response<Tournament>
                    ) {
                        TODO("Not yet implemented")
                        Log.d("Correcto", "Torneo actualizado")
                    }

                    override fun onFailure(call: Call<Tournament>, t: Throwable) {
                        Log.d("InCorrecto", "Torneo no actualizado " + t)

                    }
                })
        }
    }

    fun afegirRonda() {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        val api = retrofit.create(tournamentAPI::class.java);
        val rondaActualLocal = torneigActual.value!!.actualRound ?: 0
        val equipos = rondaActual.value!!.getGuanyadors().toList()
        var ronda = Ronda(rondaActualLocal + 1, torneigActual.value!!.id, equipos, "Abierta", emptyList())

        if (ronda.teams.size > 1) {
            ronda.crearMatches()
            api.addRound(torneigActual.value!!.id, ronda).enqueue(object : Callback<Ronda> {

                override fun onResponse(call: Call<Ronda>, response: Response<Ronda>) {

                    Log.d("Correcto", "Torneo actualizado" + response)
                    torneigActual.value?.actualRound = ronda.roundNumber
                    actualizarRondaActual()
                }

                override fun onFailure(call: Call<Ronda>, t: Throwable) {
                    Log.d("InCorrecto", "Torneo no actualizado " + t)
                }

            })
        }
    }

    fun getRondes() {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        val api = retrofit.create(tournamentAPI::class.java);
        Log.d("Hace algo", "AAAAAAA")

        api.getRounds(torneigActual.value?.id ?: "0")
            .enqueue(object : Callback<List<Ronda>> {
                override fun onResponse(call: Call<List<Ronda>>, response: Response<List<Ronda>>) {
                    if (response.isSuccessful) {
                        val rounds = response.body()
                        rondasActuales.value = rounds ?: emptyList()
                        deleteRound()
                    } else {
                        Log.d("Rounds mal", "")
                    }
                }

                override fun onFailure(call: Call<List<Ronda>>, t: Throwable) {
                    Log.d("Rounds fallo", t.toString())
                }
            })
    }

    fun updateRound(
        tournamentId: String,
        roundNumber: Int,
        round: Ronda,
        onResult: (String?) -> Unit
    ) {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        val api = retrofit.create(tournamentAPI::class.java);

        round.cerrarRonda()
        val call = api.updateRound(tournamentId, roundNumber, round)

        call.enqueue(object : Callback<Ronda> {
            override fun onResponse(call: Call<Ronda>, response: Response<Ronda>) {
                if (response.isSuccessful) {
                    onResult(null)
                } else {
                    onResult("Error updating round: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Ronda>, t: Throwable) {
                onResult("Error updating round: ${t.message}")
            }
        })
    }

    fun actualizarRondaActual() {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        val api = retrofit.create(tournamentAPI::class.java);

        val documentoId = torneigActual.value?.id ?: "0"


        val call = api.updateTournamentValues(documentoId, torneigActual.value!!)
        call.enqueue(object : Callback<Tournament> {
            override fun onResponse(call: Call<Tournament>, response: Response<Tournament>) {
                if (response.isSuccessful) {
                    // Actualización exitosa
                } else {
                    // Manejar error de respuesta
                }
            }

            override fun onFailure(call: Call<Tournament>, t: Throwable) {
                // Manejar el error
            }
        })

    }

    fun deleteRound() {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        val api = retrofit.create(tournamentAPI::class.java)

        val call = api.deleteRounds(torneigActual.value?.id?:"0")
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Log.d("Borrar bien","Bien "+torneigActual.value?.id)
                } else {
                    Log.d("Borrar error","Error")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("Borrar error","Error")
            }
        })
    }

    //TODO Hacer que al terminar se repartan los premios
    fun repartirPremios(email:String){

            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            val api = retrofit.create(userAPI::class.java);
            var user: User
            api.getUserByEmail(email)
                .enqueue(object : Callback<User> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<User>, response: Response<User>
                    ) {
                        user = response.body()!!
                        val oldTokens = user.tokens
                        val newTokens = torneigActual.value?.reward
                        val finalTokens = oldTokens + (newTokens?:0).toInt()
                        user.tokens = finalTokens
                        api.buyTokens(email, user).enqueue(object : Callback<User> {
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
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.i("Error", "$t")
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


