package cat.iesvidreres.tversus.src.data.interfaces

import androidx.lifecycle.MutableLiveData
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
    @GET("get-user/{email}")
    fun getUserByNode(@Path("email") email: String?): Call<User>
    @GET("get-players-by-tournament/{id}")
    fun showPlayers(@Path("id")id:String): Call<MutableList<User>>
    @GET("users")
    fun getUsers(): Call<MutableList<User>>
    @POST("buy-tokens/{id}")
    fun buyTokens(@Path("id")id:String,@Body user:User): Call<User>
    @POST("join-tournament/{id}")
    fun joinTournament(@Path("id")id:String,@Body user:User): Call<User>
    @POST("delete-user-from-tournament/{id}")
    fun deleteUserTournament(@Path("id")id: String,@Body user: User) : Call<User>
    @POST("update-user/{id}")
    fun updateUser(@Path("id")id: String, @Body user:User) : Call<User>
    @POST("new-user")
    fun newUserDB(@Body user:User): Call<User>
    @DELETE("delete-user/{id}")
    fun deleteUser(@Path("id")id: String) : Call<User>
    @GET("get-username/JoanAsecass")
    fun getUserByUsername() : Call<User>

}