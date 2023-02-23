package cat.iesvidreres.tversus.src.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import cat.iesvidreres.tversus.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


enum class ProviderType{
    BASIC,
    GOOGLE
}
class LoginActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 9001
    }

    val firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var auth: FirebaseAuth

    private val googleSignIn = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()) { task ->
        val signInTask = GoogleSignIn.getSignedInAccountFromIntent(task.data)
        try {
            val account = signInTask.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Autenticación exitosa
                    } else {
                        // Autenticación fallida
                    }
                }
        } catch (e: ApiException) {
            // Autenticación fallida
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        // Configurar el botón de inicio de sesión de Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        val buttonLoginGoogle : Button = findViewById(R.id.buttonLoginGoogle)

        buttonLoginGoogle.setOnClickListener {  val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        var botonLogin = findViewById<Button>(R.id.buttonLogin)

        botonLogin.setOnClickListener(){

            var email = findViewById<EditText>(R.id.editTextNameLogin)
            var pass = findViewById<EditText>(R.id.editTextNameLogin)
            loginNormal(email,pass)
        }




    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "Abierto")
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Log.d(TAG, "Intentando")
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Log.d(TAG, "User: ${user?.email}")
                } else {
                    Log.d(TAG, "No ha ido")
                }
            }
    }
    fun sesion(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email",null)
        val provider = prefs.getString("provider",null)

        if(email!=null && provider != null){
            showHome(email, ProviderType.valueOf(provider))
        }
    }
    private fun loginNormal(email: EditText, pass: EditText) {

        if (email.text.isNotEmpty()&&pass.text.isNotEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(),pass.text.toString()).addOnCompleteListener{
                if (it.isSuccessful){
                    //showHome(it.result?.user?.email ?:"",ProviderType.BASIC)
                }else{
                    //alerta()

                    val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                    prefs.putString("email", email.text.toString())
                    prefs.putString("provider", ProviderType.BASIC.toString())
                    prefs.apply()
                }
            }
        }
    }

    private fun showHome (email: String, provider: ProviderType){
        val intent = Intent(this, Activity::class.java).apply{
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(intent)
    }




}