package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.report_user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor():ViewModel() {
    private var _username = MutableLiveData<String>()
    val username get() = _username

    private var _tournamentId = MutableLiveData<String>()
    val tournamentId get() = _tournamentId

    fun setInfoReport(userName:String,tournament_id:String){
        _username.value = userName
        _tournamentId.value = tournament_id
    }
}