package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament

import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.StartTournament.MySingleton
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoTournamentViewModel @Inject constructor(
):ViewModel() {
    private var _tournament: Tournament? = null
    val tournament get() = _tournament

    fun setTournament(tournament: Tournament) {
        _tournament = tournament
        MySingleton.myTournament = tournament
    }
}