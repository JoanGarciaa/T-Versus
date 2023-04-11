package cat.iesvidreres.tversus.src.data.interfaces

import cat.iesvidreres.tversus.src.data.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface userAPI {
    @GET("get-user/{email}")
    fun getUserByEmail(@Path("email") email: String?): Call<User>

    @POST("buy-tokens/{id}")
    fun buyTokens(@Path("id")id:String,@Body user:User): Call<User>

    @POST("join-tournament/{id}")
    fun joinTournament(@Path("id")id:String,@Body user:User): Call<User>

    @GET("get-players-by-tournament/{id}")
    fun showPlayers(@Path("id")id:String): Call<MutableList<User>>

    @GET("users")
    fun getUsers(): Call<MutableList<User>>

    @DELETE("delete-user/{id}")
    fun deleteUser(@Path("id")id: String) : Call<User>

    @POST("update-user/{id}")
    fun update(@Path("id")id: String, @Body user:User) : Call<User>
}