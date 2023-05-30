package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.create_official_tournament_admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentCreateOfficialTournamentBinding
import cat.iesvidreres.tversus.src.core.ex.loseFocusAfterAction
import cat.iesvidreres.tversus.src.core.ex.onTextChanged
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.CreateTournamentViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.CreateTournamentViewState
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.model.NewTournament
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateOfficialTournamentFragment : Fragment() {
    private lateinit var binding: FragmentCreateOfficialTournamentBinding
    private val createOfficialTournamentViewModel: CreateOfficialTournamentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentCreateOfficialTournamentBinding.inflate(inflater,container,false)

        initUI()
        return binding.root
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun updateUI(viewState: CreateOfficialTournamentViewState) {

        binding.inputId.error = if (viewState.isValidId) null else "El id no es correcto"

        binding.inputName.error = if (viewState.isValidName) null else "El nombre no es correcto"

        binding.inputDescription.error = if (viewState.isValidDescription) null else "La descripcion no es correcta"

        binding.inputPrice.error = if (viewState.isValidPrice) null else "El precio no es correcto"

    }

    private fun initListeners() {

        with(binding) {

            inputIdText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputIdText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputIdText.onTextChanged { onFieldChanged() }

            inputNameText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputNameText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputNameText.onTextChanged { onFieldChanged() }

            inputDescriptionText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputDescriptionText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputDescriptionText.onTextChanged { onFieldChanged() }

            inputPriceText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputPriceText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputPriceText.onTextChanged { onFieldChanged() }

            createOfficialTournamentViewModel.onFieldsChanged(
                NewTournament(
                    id = inputIdText.text.toString(),
                    image = R.drawable.ascent,
                    name = inputNameText.text.toString(),
                    description = inputDescriptionText.text.toString(),
                    price = inputPriceText.text.toString(),
                    organizer = "admin",
                    teamsnumber = 0,
                    modality = "Individual"
                )
            )
        }
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            createOfficialTournamentViewModel.viewState.collect { viewState ->
                updateUI(viewState)
                binding.btnCreateOfficialTournament.setOnClickListener{
                    createOfficialTournamentViewModel.onFinishSelected(requireContext(),NewTournament(
                        id = binding.inputIdText.text.toString(),
                        image = R.drawable.valotourn,
                        name = binding.inputNameText.text.toString(),
                        description = binding.inputDescriptionText.text.toString(),
                        price = binding.inputPriceText.text.toString(),
                        organizer = "admin",
                        teamsnumber = 0,
                        modality = "Individual"
                    ))
                }
                createOfficialTournamentViewModel.navigateToHome.observe(requireActivity()) {
                    it.getContentIfNotHandled()?.let {
                        view?.findNavController()?.navigate(R.id.action_createOfficialTournamentFragment_to_functionsAdminFragment)
                        toast("Torneo Creado correctamente!")
                    }
                }
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        with(binding) {
            if (!hasFocus) {
                createOfficialTournamentViewModel.onFieldsChanged(
                    NewTournament(
                        id = binding.inputIdText.text.toString(),
                        image = R.drawable.ascent,
                        name = inputNameText.text.toString(),
                        description = inputDescriptionText.text.toString(),
                        price = binding.inputPriceText.text.toString(),
                        organizer = "",
                        teamsnumber = 0,
                        modality = "Individual"
                    )
                )
            }
        }
    }


}