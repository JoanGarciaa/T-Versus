package cat.iesvidreres.tversus.src.ui.auth.signin

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.ui.auth.signin.model.UserRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
):ViewModel(){
    val username = MutableLiveData<String>()
    val correu = MutableLiveData<String>()
    val contrasenya = MutableLiveData<String>()
    val dataNaixement = MutableLiveData<String>()

    fun setUser(user: String){
        username.value = user
    }

    fun setEmail( email: String){
        correu.value = email
    }
    fun setPass(pass: String){
        contrasenya.value = pass
    }
    fun setData(data: String){
        dataNaixement.value=data
    }

    fun registerUser(email: String, password: String, context: Context, userRegister: UserRegister){
        authenticationRepository.createAccount(email,password, context,userRegister)
    }



}