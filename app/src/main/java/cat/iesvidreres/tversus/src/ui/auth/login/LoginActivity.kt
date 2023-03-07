package cat.iesvidreres.tversus.src.ui.auth

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cat.iesvidreres.tversus.src.ui.home.MainActivity
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.ui.auth.signin.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var nombreLogin : String
    private val GOOGLE_SIGN_IN = 100

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        var buttonLogin: Button = findViewById(R.id.buttonLogin)
        val buttonLoginGoogle : Button = findViewById(R.id.buttonLoginGoogle)
        val buttonToRegister : Button = findViewById(R.id.btnToRegister)
        session()


        buttonToRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            var email = findViewById<EditText>(R.id.editTextEmailLogin).text
            var password = findViewById<EditText>(R.id.editTextPasswordLogin).text


            if (email.isNotEmpty()&&password.isNotEmpty()){
                nombreLogin = email.toString()
                var passwordLogin = password.toString()
            auth.signInWithEmailAndPassword(nombreLogin, passwordLogin)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if(auth.currentUser?.isEmailVerified() == true){
                            Toast.makeText(baseContext, "Bienvenido $nombreLogin", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "signInWithEmail:success")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(baseContext, "Porfavor, revisa tu bandeja de entrada y verifica el correo", Toast.LENGTH_SHORT).show()
                            Log.i("Please verifi your email","Please")
                        }
                        // Sign in success, update UI with the signed-in user's information
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }}
        }




//        buttonLoginGoogle.setOnClickListener {
//            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
//            val googleClient = GoogleSignIn.getClient(this,googleConf)
//            googleClient.signOut()
//            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
//        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)

                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(baseContext, "Login ben fet amb google", Toast.LENGTH_SHORT).show()
                            showHome(account.email ?: "")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }else {
                            Toast.makeText(baseContext, "Login mal fet amb google", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }catch (e : ApiException){
                showAlert()
            }
        }
    }

    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email",null)
        val provider = prefs.getString("provider",null)
        if (email != null && provider != null) {
            showHome(email)
        }
    }

    private fun showHome(email: String){
//        val homeIntent = Intent(this,HomeActivity::class.java).apply{
//            putExtra("email",email )
//            putExtra("provider", provider.name)
//        }
//        startActivity(homeIntent)
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            Log.i("$currentUser" , "$currentUser")
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autentificacion")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }







}