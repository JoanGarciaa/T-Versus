package cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.data.providers.firebase.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val authenticationRepository: AuthenticationRepository,
    val userRepository: UserRepository
):ViewModel(){

    fun getUserFromDB():MutableLiveData<User>{
        val user =MutableLiveData<User>()
        userRepository.findUserByEmail(authenticationRepository.getCurrentUser().email!!).observeForever{
            user.value = it
        }
        return user
    }

}