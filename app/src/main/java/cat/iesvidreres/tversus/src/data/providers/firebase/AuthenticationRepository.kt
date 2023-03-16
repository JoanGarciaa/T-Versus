package cat.iesvidreres.tversus.src.data.providers.firebase

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.ui.auth.LoginActivity
import cat.iesvidreres.tversus.src.ui.auth.signin.model.UserRegister
import cat.iesvidreres.tversus.src.ui.home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

class AuthenticationRepository @Inject constructor(
    private val userRepository: UserRepository
) {
    val auth: FirebaseAuth get() = FirebaseAuth.getInstance()

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                if (auth.currentUser?.isEmailVerified() == true) {
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                } else {

                    Toast.makeText(context,"Verifica tu cuenta con el correo" , Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
            }
        }
    }

    fun createAccount(context: Context,userRegister: UserRegister) {
        auth.createUserWithEmailAndPassword(userRegister.email, userRegister.password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                    userRepository.createUserTable(userRegister)
                    Toast.makeText(context,"Registro completado con exito! Verifica tu cuenta con el correo" , Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
            } else {
                Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    context, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun getCurrentUser(): FirebaseUser{
        return auth.currentUser!!
    }




}