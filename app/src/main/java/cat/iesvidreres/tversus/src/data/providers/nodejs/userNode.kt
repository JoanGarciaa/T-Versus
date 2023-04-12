package cat.iesvidreres.tversus.src.data.providers.nodejs

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import cat.iesvidreres.tversus.src.core.Event
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.data.providers.firebase.UserRepository
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

object userNode{

    fun getUserFromNode(email: String, callback: (User) -> Unit) {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(userAPI::class.java)

        api.getUserByNode(email).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()!!
                    callback(user) // Llama al callback con el usuario obtenido
                } else {
                    Log.e("getUserFromNode", "Error en la respuesta del servicio API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("getUserFromNode", "Error al obtener el usuario desde el servicio API", t)
            }
        })
    }

}




