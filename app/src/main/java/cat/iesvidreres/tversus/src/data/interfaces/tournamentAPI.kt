package cat.iesvidreres.tversus.src.data.interfaces

import android.graphics.Movie
import androidx.room.Update
import cat.iesvidreres.tversus.src.data.models.Ronda
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.models.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface tournamentAPI {


    @GET("tournaments")
    fun showTournaments(): Call<MutableList<Tournament>>

    @GET("get-tournament/{id}")
    fun getTournamentID(@Path("id") id: String): Call<Tournament>


    @GET("get-tournaments-type/{id}")
    fun getTypeTournaments(@Path("id") id: String):Call<MutableList<Tournament>>

    @GET("get-tournaments-type/{id}")
    fun getTournamentsTipus(@Path("id") id: String):Call<MutableList<Tournament>>

    @POST("update-tournament/{id}")
    fun updateTournament(@Path("id")id: String, @Body tournament: Tournament) : Call<Tournament>

    @POST("new-tournament")
    fun newTournament(@Body tournament: Tournament): Call<Tournament>

    @POST("new-tournament-teams")
    fun newTeamsTournament(@Body tournament: Tournament): Call<Tournament>

    @DELETE("delete-tournament/{id}")
    fun deleteTournament(@Path("id") id: String): Call<Tournament>

    @POST("/add-round/{id}")
    fun addRoundsToTournament(@Path("id") id: String, @Body rounds: List<Ronda>): Call<Tournament>
    //TODO Hacer para conseguir las rondas y match de cada equipo para ir actualizandolo
    @POST("add-round/{id}")
    fun addRound(@Path("id") id: String, @Body round: Ronda): Call<Ronda>

    @GET("rounds/{id}")
    fun getRounds(@Path("id") id: String): Call<List<Ronda>>

    @PUT("/updateRounds/{tournamentId}/{roundNumber}")
    fun updateRound(
        @Path("tournamentId") tournamentId: String,
        @Path("roundNumber") roundNumber: Int,
        @Body requestBody: Ronda
    ): Call<Ronda>

    @PUT("updateTournamentValues/{id}")
    fun updateTournamentValues(@Path("id") id: String, @Body tournament: Tournament): Call<Tournament>

    @DELETE("/deleteRounds/{tournamentId}")
    fun deleteRounds(@Path("tournamentId") tournamentId: String): Call<Unit>
}