package cat.iesvidreres.tversus.src.data.interfaces

import cat.iesvidreres.tversus.src.data.models.Tournament
import retrofit2.Call
import retrofit2.http.*

interface tournamentAPI {


    @GET("tournaments")
    fun showTournaments(): Call<MutableList<Tournament>>

    @GET("get-tournament/{id}")
    fun getTournamentID(@Path("id") id: String): Call<Tournament>

    @GET("get-tournaments-unofficial")
    fun getUnOfficialTournaments():Call<MutableList<Tournament>>

    @GET("get-tournaments-official")
    fun getOfficialTournaments():Call<MutableList<Tournament>>

    @POST("new-tournament")
    fun newTournament(@Body tournament: Tournament): Call<Tournament>

    @DELETE("delete-tournament/{id}")
    fun deleteTournament(@Path("id") id: String): Call<Tournament>
}