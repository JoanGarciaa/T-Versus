package cat.iesvidreres.tversus.src.ui.auth.signin

import androidx.lifecycle.MutableLiveData
import java.util.*

class RegisterViewModel {
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
}