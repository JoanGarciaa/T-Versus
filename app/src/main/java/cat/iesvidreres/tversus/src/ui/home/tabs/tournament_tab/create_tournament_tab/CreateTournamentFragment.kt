package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentCreateTournamentBinding
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.model.NewTournament
import cat.iesvidreres.tversus.src.core.ex.*
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import kotlin.random.Random

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
    }

    private fun initListeners() {

        with(binding) {

            inputNameText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputNameText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputNameText.onTextChanged { onFieldChanged() }

            inputDescriptionText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputDescriptionText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputDescriptionText.onTextChanged { onFieldChanged() }


            createTournamentViewModel.onFieldsChanged(
                NewTournament(
                    image = R.drawable.ascent,
                    name = inputNameText.text.toString(),
                    description = inputDescriptionText.text.toString(),
                    price = 0,
                    organizer = createTournamentViewModel.authenticationRepository.getCurrentUser().email.toString()
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
                binding.btnCreateNewTournament.setOnClickListener{
                    createTournamentViewModel.onFinishSelected(requireContext(),NewTournament(
                        image = R.drawable.valotourn,
                        name = binding.inputNameText.text.toString(),
                        description = binding.inputDescriptionText.text.toString(),
                        price = 0,
                        organizer = createTournamentViewModel.authenticationRepository.getCurrentUser().email.toString()
                    ))
                    Toast.makeText(requireContext(),"Torneo creado",Toast.LENGTH_SHORT).show()
                    Thread.sleep(500)
                    view?.findNavController()?.navigate(R.id.action_createTournamentFragment_to_homeFragment)
                }
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        with(binding) {
            if (inputNameText.text.toString().isNotEmpty() && inputDescriptionText.text.toString()
                    .isNotEmpty()
            )
            if (!hasFocus) {
                createTournamentViewModel.onFieldsChanged(
                    NewTournament(
                        image = R.drawable.ascent,
                        name = inputNameText.text.toString(),
                        description = inputDescriptionText.text.toString(),
                        price = 0,
                        organizer = createTournamentViewModel.authenticationRepository.getCurrentUser().email.toString()
                    )
                )
            }
        }
    }

//    fun fieldsTournaments(): Tournament{
//        var name = binding.inputNameText.text.toString()
//        var description = binding.inputDescriptionText.text.toString()
//        val id = Random.nextDouble(1000000.0, 20000000.0).toString()
//        val organizator = createTournamentViewModel.authenticationRepository.getCurrentUser()
//
//        val tournament = Tournament(name,id,"Valorant", organizator.toString(),description,0,R.drawable.valotourn,"unofficial")
//
//        return tournament
//    }
//    private fun retrofit() {
//        fieldsTournaments()
//        val gson = GsonBuilder().setLenient().create()
//        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
//            .addConverterFactory(GsonConverterFactory.create(gson)).build()
//        var new : Tournament
//        val api = retrofit.create(tournamentAPI::class.java);
//        api.newTournament(fieldsTournaments()).enqueue(object : Callback<Tournament> {
//            override fun onResponse(
//                call: Call<Tournament>, response: Response<Tournament>
//            ) {
//                new = response.body()!!
//                Log.i("asd","$new")
//            }
//
//            override fun onFailure(call: Call<Tournament>, t: Throwable) {
//                Log.i("Erroddr","$t")
//            }
//
//        })
//        Log.i("guay","guay")
//    }

}