package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentCreateTournamentBinding
import cat.iesvidreres.tversus.src.clasesVisuales.CustomSpinnerAdapter
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.model.NewTournament
import cat.iesvidreres.tversus.src.core.ex.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateTournamentFragment : Fragment() {
    private lateinit var binding: FragmentCreateTournamentBinding
    private val createTournamentViewModel: CreateTournamentViewModel by viewModels()
    lateinit var numeroEquips: Spinner
    lateinit var categoria: Spinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateTournamentBinding.inflate(inflater, container, false)

        initUI()

        setSpinner()
        spinnerModalidades()

        return binding.root
    }

    private fun spinnerModalidades(){
        categoria = binding.spinnerModalitat
        val spinnerOpciones = listOf(
            "Individual",
            "Teams")

        val adapter = CustomSpinnerAdapter(requireContext(), spinnerOpciones)
        categoria.adapter = adapter

        categoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = spinnerOpciones[position]

                // Realizar acciones basadas en el valor seleccionado
                if (selectedItem == "Teams") {
                    binding.spinnerQuantitat.visibility = View.VISIBLE
                } else {
                    binding.spinnerQuantitat.visibility = View.GONE
                }
                }


            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se seleccionó ningún elemento
            }
        }
    }
    private fun setSpinner(){
        numeroEquips = binding.spinnerQuantitat
        val spinnerOpciones = listOf(
            "4",
            "8",
            "16")

        val adapter = CustomSpinnerAdapter(requireContext(), spinnerOpciones)
        numeroEquips.adapter = adapter


      //  val adapterSpinner: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, activityTypes)
      //  adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
  //      numeroEquips.adapter = adapterSpinner
        numeroEquips.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedActivityType = parent.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {

        with(binding) {
            spinnerModalitat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedModalidad = parent.getItemAtPosition(position).toString()
                    onFieldChanged()
                    Log.d("aa", selectedModalidad)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

            spinnerQuantitat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    onFieldChanged()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

            inputNameText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputNameText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputNameText.onTextChanged { onFieldChanged() }

            inputDescriptionText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputDescriptionText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputDescriptionText.onTextChanged { onFieldChanged() }


            createTournamentViewModel.onFieldsChanged(
                NewTournament(
                    id = "",
                    image = R.drawable.ascent,
                    name = inputNameText.text.toString(),
                    description = inputDescriptionText.text.toString(),
                    price = "0",
                    organizer = createTournamentViewModel.authenticationRepository.getCurrentUser().email.toString(),
                    teamsnumber = 0,
                    modality = "Individual"
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
                    Log.d("",binding.spinnerModalitat.selectedItem.toString())
                    createTournamentViewModel.onFinishSelected(requireContext(),NewTournament(
                        id = "",
                        image = R.drawable.valotourn,
                        name = binding.inputNameText.text.toString(),
                        description = binding.inputDescriptionText.text.toString(),
                        price = "0",
                        organizer = createTournamentViewModel.authenticationRepository.getCurrentUser().email.toString(),
                        teamsnumber = binding.spinnerQuantitat.selectedItem.toString().toInt(),
                        modality = binding.spinnerModalitat.selectedItem.toString()
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
                        id = "",
                        image = R.drawable.ascent,
                        name = inputNameText.text.toString(),
                        description = inputDescriptionText.text.toString(),
                        price = "0",
                        organizer = createTournamentViewModel.authenticationRepository.getCurrentUser().email.toString(),
                        teamsnumber = spinnerQuantitat.selectedItem.toString().toInt(),
                        modality = spinnerModalitat.selectedItem.toString()
                    )
                )
            }
        }
    }

}