package cat.iesvidreres.tversus.src.ui.auth.signin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.ActivityLoginBinding
import cat.iesvidreres.tversus.databinding.ActivityRegisterBinding
import cat.iesvidreres.tversus.src.ui.auth.LoginActivity
import cat.iesvidreres.tversus.src.ui.auth.login.LoginViewModel
import cat.iesvidreres.tversus.src.ui.auth.signin.model.UserRegister
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userText: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var dataText: EditText
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userText = findViewById(R.id.input_nickname_registre_text)
        emailText = findViewById(R.id.input_email_registre_text)
        passwordText = findViewById(R.id.input_password_registre_text)
        dataText = findViewById(R.id.input_borndate_registre_text)

        if (savedInstanceState != null) {
            registerViewModel.setUser(savedInstanceState.getString("user", ""))
            registerViewModel.setPass(savedInstanceState.getString("pass", ""))
            registerViewModel.setEmail(savedInstanceState.getString("email", ""))
            registerViewModel.setData(savedInstanceState.getString("data", ""))
        }

        var buttonToLogin: Button = findViewById(R.id.buttonToLogin)
        var buttonRegister: Button = findViewById(R.id.btnRegister)


        buttonRegister.setOnClickListener {
            val email = binding.inputEmailRegistreText.text.toString()
            val password = binding.inputPasswordRegistreText.text.toString()
            val nickname = binding.inputNicknameRegistreText.text.toString()
            val years = binding.inputBorndateRegistreText.text.toString()
            registerViewModel.registerUser(this, UserRegister(nickname, email, password, years))
        }

        buttonToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //Observadores
//        viewModel.correu.observe(this) { text ->
//            emailText.setText(text)
//        }
//
//        viewModel.contrasenya.observe(this) { text ->
//            passwordText.setText(text)
//        }
//
//        viewModel.username.observe(this) { text ->
//            userText.setText(text)
//        }
//
//        viewModel.dataNaixement.observe(this) { text ->
//            dataText.setText(text)
//        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    //Guardar datos al cerrar activity
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        viewModel.setUser(userText.text.toString())
//
//        viewModel.setEmail(emailText.text.toString())
//
//        viewModel.setPass(passwordText.text.toString())
//
//        viewModel.setData(dataText.text.toString())
//
//        outState.putString("user", viewModel.username.value)
//        Log.d(TAG, "Valor user "+viewModel.username.value)
//        outState.putString("email", viewModel.correu.value)
//        Log.d(TAG, "Valor correo "+viewModel.correu.value)
//        outState.putString("pass", viewModel.contrasenya.value)
//        Log.d(TAG, "Valor pass "+viewModel.contrasenya.value)
//        outState.putString("data", viewModel.dataNaixement.value)
//        Log.d(TAG, "Valor data "+viewModel.dataNaixement.value)
//    }

}