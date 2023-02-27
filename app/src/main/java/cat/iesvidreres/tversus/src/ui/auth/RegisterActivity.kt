package cat.iesvidreres.tversus.src.ui.auth

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cat.iesvidreres.tversus.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth


        var buttonToLogin: Button = findViewById(R.id.buttonToLogin)
        var buttonRegister: Button = findViewById(R.id.buttonRegister)


        buttonRegister.setOnClickListener {
            var nombreRegister = findViewById<EditText>(R.id.editTextNameRegister).text.toString()
            var passwordRegister =
                findViewById<EditText>(R.id.editTextPasswRegister).text.toString()
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
                }
        }

        buttonToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }


}