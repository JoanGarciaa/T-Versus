package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.list_tournaments_admin.info_tournament_admin

import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class InfoTournamentAdminViewModel @Inject constructor(): ViewModel() {
    private var _user: User? = null
    val user get() = _user

    fun setUser(user: User) {
        user.isJoined = false
        _user = user
    }
}