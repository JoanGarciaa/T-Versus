package cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.data.providers.firebase.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val authenticationRepository: AuthenticationRepository,
    val userRepository: UserRepository,
    //val user: User
):ViewModel(){
    private var _user: User? = null
    val user get() = _user
    fun retrofit() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(userAPI::class.java);
        lateinit var user:User
        api.getUserByEmail(authenticationRepository.getCurrentUserEmail().email.toString()).enqueue(object :
            Callback<User> {
            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                user = response.body()!!
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.i("Error","$t")
            }

        })
    }

    fun getUserFromDB():MutableLiveData<User>{
        val user =MutableLiveData<User>()
        userRepository.findUserByEmail(authenticationRepository.getCurrentUser().email!!).observeForever{
            user.value = it
        }
        return user
    }

}