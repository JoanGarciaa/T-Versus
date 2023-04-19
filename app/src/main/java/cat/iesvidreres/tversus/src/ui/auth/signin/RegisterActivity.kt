package cat.iesvidreres.tversus.src.ui.auth.signin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.ActivityRegisterBinding
import cat.iesvidreres.tversus.src.core.ex.loseFocusAfterAction
import cat.iesvidreres.tversus.src.core.ex.onTextChanged
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.ui.auth.LoginActivity
import cat.iesvidreres.tversus.src.ui.auth.signin.model.UserRegister
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.CreateTournamentViewState
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.model.NewTournament
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initUI()
        binding.buttonToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initUI(){
        initListeners()
        initObservers(this)
    }

    private fun initListeners() {

        with(binding) {

            inputEmailRegistreText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputEmailRegistreText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputEmailRegistreText.onTextChanged { onFieldChanged() }

            inputPasswordRegistreText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputPasswordRegistreText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputPasswordRegistreText.onTextChanged { onFieldChanged() }

            inputNicknameRegistreText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputNicknameRegistreText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputNicknameRegistreText.onTextChanged { onFieldChanged() }

            inputBorndateRegistreText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputBorndateRegistreText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputBorndateRegistreText.onTextChanged { onFieldChanged() }


            registerViewModel.onFieldsChanged(
                UserRegister(
                    nickname = inputNicknameRegistreText.text.toString(),
                    email = inputEmailRegistreText.text.toString(),
                    password = inputPasswordRegistreText.text.toString(),
                    years = inputBorndateRegistreText.text.toString()
                )
            )
        }
    }

    private fun updateUI(viewState: RegisterViewState) {
        binding.inputEmailRegistre.error = if (viewState.isValidEmail) null else "El email tiene un formato incorrecto"

        binding.inputNicknameRegistre.error = if (viewState.isValidNickName) null else "El nombre no esta disponible"

        binding.inputPasswordRegistre.error = if (viewState.isValidPassword) null else "La contraseña es demasiado débil"

        binding.inputBorndateRegistre.error = if (viewState.isValidBornDate) null else "La fecha no tiene un formato correcto"

    }

    private fun initObservers(context: Context) {
        lifecycleScope.launchWhenStarted {
            registerViewModel.viewState.collect { viewState ->
                updateUI(viewState)
                binding.btnRegister.setOnClickListener{
                    registerViewModel.onSignInSelected(context,UserRegister(
                        nickname = binding.inputNicknameRegistreText.text.toString(),
                        email = binding.inputEmailRegistreText.text.toString(),
                        password = binding.inputPasswordRegistreText.text.toString(),
                        years = binding.inputBorndateRegistreText.text.toString()
                    )
                    )
                }
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        with(binding) {
            if (!hasFocus) {
                registerViewModel.onFieldsChanged(
                    UserRegister(
                        nickname = binding.inputNicknameRegistreText.text.toString(),
                        email = binding.inputEmailRegistreText.text.toString(),
                        password = binding.inputPasswordRegistreText.text.toString(),
                        years = binding.inputBorndateRegistreText.text.toString()
                    )
                )
            }
        }
    }


}