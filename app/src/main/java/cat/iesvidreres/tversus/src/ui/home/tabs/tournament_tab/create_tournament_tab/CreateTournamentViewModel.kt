package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.data.providers.firebase.UserRepository
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileFragment
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
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
class CreateTournamentViewModel @Inject constructor(
    val authenticationRepository: AuthenticationRepository,
    val userRepository: UserRepository,
    //val profileFragment: ProfileFragment
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

    fun onFinishSelected(context: Context,newTournament: NewTournament){
        val viewState = newTournament.toNewTournamentState()
        if (viewState.createTournamentValidated()) {
            val id = Random.nextDouble(1000000.0, 20000000.0).toString()
            var name = newTournament.name
            var description = newTournament.description
            //var organizator = profileFragment.user.email

            val tournament = Tournament(name,id,"Valorant","s",description,0, R.drawable.valotourn,"unofficial")


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
                    Log.i("asd","$new")
                }

                override fun onFailure(call: Call<Tournament>, t: Throwable) {
                    Log.i("Erroddr","$t")
                }

            })
            Log.i("guay","guay")

        }else{
            onFieldsChanged(newTournament)
            Toast.makeText(context, "¡El torneo necesita un nombre y una descripcion!", Toast.LENGTH_SHORT).show()
        }
    }


}