package cat.iesvidreres.tversus.src.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import cat.iesvidreres.tversus.src.ui.home.MainActivity
import cat.iesvidreres.tversus.databinding.ActivityLoginBinding
import cat.iesvidreres.tversus.src.core.ex.loseFocusAfterAction
import cat.iesvidreres.tversus.src.core.ex.onTextChanged
import cat.iesvidreres.tversus.src.ui.auth.login.LoginViewModel
import cat.iesvidreres.tversus.src.ui.auth.login.LoginViewState
import cat.iesvidreres.tversus.src.ui.auth.signin.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val GOOGLE_SIGN_IN = 100
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        binding.btnToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        initUI()

    }

    private fun initUI(){
        initListeners()
        initObservers()
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    baseContext,
                                    "Login ben fet amb google",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    baseContext,
                                    "Login mal fet amb google",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            } catch (e: ApiException) {
                showAlert()
            }
        }
    }

    private fun updateUI(viewState: LoginViewState) {
        with(binding) {
            inputEmailRegistre.error =
                if (viewState.isValidEmail) null else "El correo no es valido"
            inputPasswordRegistre.error =
                if (viewState.isValidPassword) null else "La contraseña no es válida"
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            loginViewModel.onFieldsChanged(
                email = binding.inputEmailRegistreText.text.toString(),
                password = binding.inputPasswordRegistreText.text.toString()
            )
        }
    }

    private fun initListeners() {

        binding.inputEmailRegistreText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.inputEmailRegistreText.onTextChanged { onFieldChanged() }

        binding.inputPasswordRegistreText.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
        binding.inputPasswordRegistreText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.inputPasswordRegistreText.onTextChanged { onFieldChanged() }

        binding.buttonLogin.setOnClickListener {
            loginViewModel.onLoginSelected(
                binding.inputEmailRegistreText.text.toString(),
                binding.inputPasswordRegistreText.text.toString(),
                this
            )

        }
    }


    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.viewState.collect { viewState ->
                updateUI(viewState)
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.i("$currentUser", "$currentUser")
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autentificacion")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}