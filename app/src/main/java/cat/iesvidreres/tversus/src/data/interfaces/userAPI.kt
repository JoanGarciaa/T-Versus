package cat.iesvidreres.tversus.src.data.interfaces

import cat.iesvidreres.tversus.src.data.models.User
import retrofit2.Call
import retrofit2.http.Body
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

//    @GET("user")
//    fun getUsers(): Call<User>
}