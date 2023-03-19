package cat.iesvidreres.tversus.src.data.interfaces

import androidx.lifecycle.MutableLiveData
import cat.iesvidreres.tversus.src.data.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface userAPI {
    @GET("get-user/{email}")
    fun getUserByEmail(@Path("email") email: String?): Call<User>

    @GET("user")
    fun getUsers(): Call<User>
}