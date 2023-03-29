package cat.iesvidreres.tversus.src.data.interfaces

import cat.iesvidreres.tversus.src.data.models.Tournament
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface tournamentAPI {
    @POST("new-tournament")
    fun newTournament(@Body tournament: Tournament): Call<Tournament>

    @GET("tournaments")
    fun showTournaments(): Call<MutableList<Tournament>>

    @GET("get-tournament/{id}")
    fun getTournamentID(@Path("id") id: String): Call<Tournament>
}