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
                }
                createTournamentViewModel.navigateToHome.observe(requireActivity()) {
                    it.getContentIfNotHandled()?.let {
                        view?.findNavController()?.navigate(R.id.action_createTournamentFragment_to_homeFragment)
                        toast("Torneo Creado!")
                    }
                }
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        with(binding) {
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

}