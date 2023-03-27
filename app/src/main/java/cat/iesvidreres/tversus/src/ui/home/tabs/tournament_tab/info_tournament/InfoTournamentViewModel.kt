package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament

import android.util.Log
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class InfoTournamentViewModel @Inject constructor(
):ViewModel() {
    private var _tournament: Tournament? = null
    val tournament get() = _tournament

    fun setTournament(tournament: Tournament) {
        _tournament = tournament
    }


}