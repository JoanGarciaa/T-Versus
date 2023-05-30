package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.unofficial_tournament_tab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.providers.firebase.TournamentRepository

class UnOfficialTournamentViewModel: ViewModel() {
    var tournamentLiveDataList: LiveData<List<Tournament>> = MutableLiveData<List<Tournament>>()
    var tournamentListData: MutableLiveData<List<Tournament>> = MutableLiveData<List<Tournament>>()
    var repositorio: TournamentRepository = TournamentRepository()

    fun getUnoficials(){
        tournamentLiveDataList = repositorio.getUnoficials()

    }
}