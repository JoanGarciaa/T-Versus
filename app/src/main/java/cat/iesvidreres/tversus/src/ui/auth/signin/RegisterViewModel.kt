package cat.iesvidreres.tversus.src.ui.auth.signin

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.ui.auth.signin.model.UserRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
):ViewModel(){

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
        const val BORN_LENGTH = 9
    }

    private val _viewState = MutableStateFlow(RegisterViewState())
    val viewState: StateFlow<RegisterViewState>
        get() = _viewState

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

    fun registerUser(context: Context, userRegister: UserRegister){

    }

    fun onSignInSelected(context: Context,userRegister: UserRegister) {
        val viewState = userRegister.toSignInViewState()
        if (viewState.userValidated() && userRegister.isNotEmpty()) {
            signInUser(context,userRegister)
        } else {
            onFieldsChanged(userRegister)
        }
    }

    private fun signInUser(context:Context,userRegister: UserRegister) {
        viewModelScope.launch {
            authenticationRepository.createAccount(context,userRegister)
        }
    }

    private fun UserRegister.toSignInViewState(): RegisterViewState {
        return RegisterViewState(
            isValidEmail = isValidOrEmptyEmail(email),
            isValidPassword = isValidOrEmptyPassword(password),
            isValidNickName = isValidName(nickname),
            isValidBornDate = isValidBorndate(years)
        )
    }

    fun onFieldsChanged(userRegister: UserRegister) {
        _viewState.value = userRegister.toSignInViewState()
    }

    private fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidOrEmptyPassword(password: String): Boolean =
        (password.length >= MIN_PASSWORD_LENGTH) || password.isEmpty()

    private fun isValidName(name: String): Boolean =
        name.length >= MIN_PASSWORD_LENGTH || name.isEmpty()

    private fun isValidBorndate(date: String): Boolean =
        date.length == BORN_LENGTH || date.isEmpty()



}