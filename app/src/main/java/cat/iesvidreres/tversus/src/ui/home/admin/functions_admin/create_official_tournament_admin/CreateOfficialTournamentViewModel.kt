package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.create_official_tournament_admin

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.core.Event
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.CreateTournamentViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.CreateTournamentViewState
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.model.NewTournament
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CreateOfficialTournamentViewModel @Inject constructor(
    val authenticationRepository: AuthenticationRepository,
):ViewModel() {
    private companion object{
        const val ID_LENGTH = 3
        const val NAME_LENGTH = 4
        const val DESCRIPTION_LENGHT = 8
    }

    private val _viewState = MutableStateFlow(CreateOfficialTournamentViewState())
    val viewState: StateFlow<CreateOfficialTournamentViewState>
        get() = _viewState

    private val _navigateToHome = MutableLiveData<Event<Boolean>>()
    val navigateToHome: LiveData<Event<Boolean>>
        get() = _navigateToHome


    fun onFieldsChanged(newTournament: NewTournament) {
        _viewState.value = newTournament.toNewTournamentState()
    }
    fun isValidId(id: String) = id.length >= ID_LENGTH
    fun isValidName(name: String) = name.length >= NAME_LENGTH
    fun isValidDescription(description: String) = description.length >= DESCRIPTION_LENGHT
    fun isValidPrice(price: String) = price >= 1.toString()


    fun NewTournament.toNewTournamentState(): CreateOfficialTournamentViewState {
        return CreateOfficialTournamentViewState(
            isValidId = isValidId(id),
            isValidName = isValidName(name),
            isValidDescription = isValidDescription(description),
            isValidPrice = isValidPrice(price)
        )
    }

    fun onFinishSelected(context: Context, newTournament: NewTournament){
        val viewState = newTournament.toNewTournamentState()
        if (viewState.tournamentOfficialValidated() && newTournament.isNotEmpty()) {
            val id = newTournament.id
            val name = newTournament.name
            val description = newTournament.description
            val price = newTournament.price
            val teamsNumber = newTournament.teamsnumber
            val tournament = Tournament(name,id,"Valorant","admin",description,price.toInt(), R.drawable.valotourn,"official",
                mutableListOf(),
                emptyList(),
                teamsNumber,0.0)

            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            var new : Tournament
            val api = retrofit.create(tournamentAPI::class.java);
            api.newTournament(tournament).enqueue(object : Callback<Tournament> {
                override fun onResponse(
                    call: Call<Tournament>, response: Response<Tournament>
                ) {
                    new = response.body()!!
                }

                override fun onFailure(call: Call<Tournament>, t: Throwable) {
                    Log.i("Erroddr","$t")
                }

            })
            _navigateToHome.value = Event(true)
        }else{
            onFieldsChanged(newTournament)
            Toast.makeText(context, "¡El torneo necesita un nombre y una descripcion!", Toast.LENGTH_SHORT).show()
        }
    }


}