package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab

import android.util.Log
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.data.providers.firebase.UserRepository
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.model.NewTournament
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CreateTournamentViewModel @Inject constructor(
    val authenticationRepository: AuthenticationRepository,
    val userRepository: UserRepository
):ViewModel(){

    private companion object{
        const val NAME_LENGTH = 4
        const val DESCRIPTION_LENGHT = 8
    }

    private val _viewState = MutableStateFlow(CreateTournamentViewState())
    val viewState: StateFlow<CreateTournamentViewState>
        get() = _viewState

    fun onFieldsChanged(newTournament: NewTournament) {
        _viewState.value = newTournament.toNewTournamentState()
    }

    fun isValidName(name: String) = name.length >= NAME_LENGTH

    fun isValidDescription(description: String) = description.length >= DESCRIPTION_LENGHT


    fun NewTournament.toNewTournamentState(): CreateTournamentViewState {
        return CreateTournamentViewState(
            isValidName = isValidName(name),
            isValidDescription = isValidDescription(description)
        )
    }

    fun getUser(){
        userRepository.findUserByEmail(authenticationRepository.getCurrentUser().email!!)
    }

    fun onFinishSelected(){

    }


}