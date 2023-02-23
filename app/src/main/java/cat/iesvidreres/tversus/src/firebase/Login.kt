package cat.iesvidreres.tversus.src.firebase

import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import cat.iesvidreres.tversus.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

enum class ProviderType{
    BASIC,
    GOOGLE
}
class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

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


        val opcionesInicioSesion = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, opcionesInicioSesion)

        var botonLoginGoogle = findViewById<Button>(R.id.buttonLoginGoogle)

        botonLoginGoogle.setOnClickListener {

            val signInIntent = googleSignInClient.signInIntent

            val intentSender = signInIntent?.let { PendingIntent.getActivity(this, 0, it, 0)?.intentSender }

            intentSender?.let {
                val intentSenderRequest = IntentSenderRequest.Builder(it).build()
                googleSignIn.launch(intentSenderRequest)
            }
        }

        // Mirar si ya tiene cuenta
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Autenticación exitosa
                    } else {
                        // Autenticación fallida
                    }
                }
        }

     /*   var botonCerrarGoogle :Button = findViewById(R.id.b)

        botonCerrarGoogle.setOnClickListener {

            val googleSignInClient = GoogleSignIn.getClient(this, opcionesInicioSesion)
            googleSignInClient.signOut().addOnCompleteListener {
                // Cierre de sesión exitoso
            }.addOnFailureListener { e ->
                // Ocurrió un error al cerrar sesión
            }
        }*/

    }}