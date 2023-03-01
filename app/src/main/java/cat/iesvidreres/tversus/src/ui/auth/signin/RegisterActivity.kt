package cat.iesvidreres.tversus.src.ui.auth.signin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: RegisterViewModel

    private lateinit var userText: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var dataText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        viewModel= RegisterViewModel()

        userText = findViewById(R.id.input_nickname_registre_text)
        emailText = findViewById(R.id.input_email_registre_text)
        passwordText = findViewById(R.id.input_password_registre_text)
        dataText = findViewById(R.id.input_borndate_registre_text)

        if (savedInstanceState != null) {
            viewModel.setUser(savedInstanceState.getString("user", ""))
            viewModel.setPass(savedInstanceState.getString("pass", ""))
            viewModel.setEmail(savedInstanceState.getString("email", ""))
            viewModel.setData(savedInstanceState.getString("data", ""))
        }

        var buttonToLogin: Button = findViewById(R.id.buttonToLogin)
        var buttonRegister: Button = findViewById(R.id.btnRegister)


        buttonRegister.setOnClickListener {
            var nombre = findViewById<EditText>(R.id.input_email_registre_text).text
            var password = findViewById<EditText>(R.id.input_password_registre_text).text

            if (nombre.isNotEmpty()&&password.isNotEmpty()){

            //TODO Hacer que se guarden los datos del usario en la base de datos

            var nombreRegister = nombre.toString()
            var passwordRegister = password.toString()

            auth.createUserWithEmailAndPassword(nombreRegister, passwordRegister)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                            Toast.makeText(this,"Registro completado con exito! Verifica tu cuenta con el correo" , Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "createUserWithEmail:success")
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }}
        }

        buttonToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //Observadores
        viewModel.correu.observe(this) { text ->
            emailText.setText(text)
        }

        viewModel.contrasenya.observe(this) { text ->
            passwordText.setText(text)
        }

        viewModel.username.observe(this) { text ->
            userText.setText(text)
        }

        viewModel.dataNaixement.observe(this) { text ->
            dataText.setText(text)
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }

    //Guardar datos al cerrar activity
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel.setUser(userText.text.toString())

        viewModel.setEmail(emailText.text.toString())

        viewModel.setPass(passwordText.text.toString())

        viewModel.setData(dataText.text.toString())

        outState.putString("user", viewModel.username.value)
        Log.d(TAG, "Valor user "+viewModel.username.value)
        outState.putString("email", viewModel.correu.value)
        Log.d(TAG, "Valor correo "+viewModel.correu.value)
        outState.putString("pass", viewModel.contrasenya.value)
        Log.d(TAG, "Valor pass "+viewModel.contrasenya.value)
        outState.putString("data", viewModel.dataNaixement.value)
        Log.d(TAG, "Valor data "+viewModel.dataNaixement.value)
    }

}