package cat.iesvidreres.tversus.src.ui.auth.signin

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.ui.auth.signin.model.UserRegister
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
        const val BORN_LENGTH = 10
    }

    private val _viewState = MutableStateFlow(RegisterViewState())
    val viewState: StateFlow<RegisterViewState>
        get() = _viewState

    fun onFieldsChanged(userRegister: UserRegister) {
        _viewState.value = userRegister.toSignInViewState()
    }

    fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    fun isValidOrEmptyPassword(password: String): Boolean =
        (password.length >= MIN_PASSWORD_LENGTH) || password.isEmpty()

    fun isValidName(name: String): Boolean =
        name.length >= MIN_PASSWORD_LENGTH || name.isEmpty()

    fun isValidBorndate(date: String): Boolean =
        date.length == BORN_LENGTH || date.isEmpty()


    private fun UserRegister.toSignInViewState(): RegisterViewState {
        return RegisterViewState(
            isValidEmail = isValidOrEmptyEmail(email),
            isValidPassword = isValidOrEmptyPassword(password),
            isValidNickName = isValidName(nickname),
            isValidBornDate = isValidBorndate(years)
        )
    }


    fun onSignInSelected(context: Context, userRegister: UserRegister) {
        val viewState = userRegister.toSignInViewState()
        if (viewState.userValidated() && userRegister.isNotEmpty()) {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            var user = User(
                userRegister.nickname,
                userRegister.email,
                userRegister.password,
                userRegister.years,
                9,
                "",
                "https://imagen.research.google/main_gallery_images/a-brain-riding-a-rocketship.jpg",
                false,
                0
            )
            val api = retrofit.create(userAPI::class.java);
            api.newUserDB(user).enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>, response: Response<User>
                ) {
                    user = response.body()!!
                    signInUser(context, userRegister)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.i("Error", "$t")
                    signInUser(context, userRegister)
                }
            })
        } else {
            onFieldsChanged(userRegister)
            Log.e("$userRegister", "$userRegister")
            Toast.makeText(context, "¡Completa todos los campos!", Toast.LENGTH_SHORT).show()
        }
    }

    fun signInUser(context: Context, userRegister: UserRegister) {
        viewModelScope.launch {
            authenticationRepository.createAccount(context, userRegister)
        }
    }


}