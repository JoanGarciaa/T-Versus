package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentCreateTournamentBinding
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.model.NewTournament
import cat.iesvidreres.tversus.src.core.ex.*
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class CreateTournamentFragment : Fragment() {
    private lateinit var binding: FragmentCreateTournamentBinding
    private val createTournamentViewModel: CreateTournamentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateTournamentBinding.inflate(inflater, container, false)

        initUI()

        return binding.root
    }

    private fun initUI() {
        initListeners()
        initObservers()
        retrofit()
    }

    private fun initListeners() {

        with(binding) {

            inputNameText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputNameText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputNameText.onTextChanged { onFieldChanged() }

            inputDescriptionText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputDescriptionText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputDescriptionText.onTextChanged { onFieldChanged() }

            btnCreateNewTournament.setOnClickListener{
                createTournamentViewModel.getUser()
            }

            createTournamentViewModel.onFieldsChanged(
                NewTournament(
                    image = R.drawable.ascent,
                    name = inputNameText.text.toString(),
                    description = inputDescriptionText.text.toString(),
                    price = 0
                )
            )
        }
    }

    private fun updateUI(viewState: CreateTournamentViewState) {
        binding.inputName.error = if (viewState.isValidName) null else "El nombre no es correcto"

        binding.inputDescription.error =
            if (viewState.isValidDescription) null else "La descripcion no es correcta"

    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            createTournamentViewModel.viewState.collect { viewState ->
                updateUI(viewState)
                viewState.createTournamentValidated()
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        with(binding) {
            if (inputNameText.text.toString().isNotEmpty() && inputDescriptionText.text.toString()
                    .isNotEmpty()
            ) {

            }
            if (!hasFocus) {
                createTournamentViewModel.onFieldsChanged(
                    NewTournament(
                        image = R.drawable.ascent,
                        name = inputNameText.text.toString(),
                        description = inputDescriptionText.text.toString(),
                        price = 0
                    )
                )
            }
        }

    }

    private fun retrofit() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        var tournament = Tournament("asd","2","asd","asdd","asd",2,1)

        val api = retrofit.create(tournamentAPI::class.java);
        var userList: Tournament
        api.newTournament(tournament).enqueue(object : Callback<Tournament> {
            override fun onResponse(
                call: Call<Tournament>, response: Response<Tournament>
            ) {
                Log.i("asd","$tournament")
                userList = response.body()!!
                Log.i("comanem","$userList")


            }

            override fun onFailure(call: Call<Tournament>, t: Throwable) {
                Log.i("Erroddr","$t")
            }

        })

    }

}