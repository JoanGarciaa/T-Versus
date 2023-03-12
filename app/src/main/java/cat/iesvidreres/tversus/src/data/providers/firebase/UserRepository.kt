package cat.iesvidreres.tversus.src.data.providers.firebase

import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.ui.auth.signin.model.UserRegister
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class UserRepository @Inject constructor(){
    val db = Firebase.firestore

    fun createUserTable(userRegister: UserRegister){
        val user = User(userRegister.nickname, userRegister.email,userRegister.password,userRegister.years,0 )
        db.collection("users").document(user.email).set(user)
    }
}