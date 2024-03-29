package cat.iesvidreres.tversus.src.data.providers.firebase

import androidx.lifecycle.MutableLiveData
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.ui.auth.signin.model.UserRegister
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class UserRepository @Inject constructor(){
    val db = Firebase.firestore

    companion object {
        const val USER_COLLECTION = "users"
        const val USERNAME_FIELD = "username"
        const val EMAIL_FIELD = "email"
        const val PASSWORD_FIELD = "password"
        const val BORN_FIELD = "borndate"
        const val TOKEN_FIELD = "tokens"
        const val TOURNAMENT_ID = "tournament_id"
        const val IS_JOINED = "isJoined"
        const val IMAGE = "image"
        const val POINTS = "points"
    }

    fun createUserTable(userRegister: UserRegister){
        val user = User(userRegister.nickname, userRegister.email,userRegister.password,userRegister.years,0 , "", isJoined = false, image = "",points = 0)
        db.collection("users").document(user.email).set(user)
    }
    fun findUserByEmail(email:String):MutableLiveData<User>{
        val user = MutableLiveData<User>()
        db.collection(USER_COLLECTION)
            .whereEqualTo(EMAIL_FIELD,email)
            .get()
            .addOnSuccessListener {
            user.value = User(
                it.documents.first().getString(USERNAME_FIELD)!!,
                it.documents.first().getString(EMAIL_FIELD)!!,
                it.documents.first().getString(PASSWORD_FIELD)!!,
                it.documents.first().getString(BORN_FIELD)!!,
                it.documents.first().getField<Int>(TOKEN_FIELD)!!,
                it.documents.first().getString(TOURNAMENT_ID)!!,
                it.documents.first().getString(IMAGE)!!,
                it.documents.first().getBoolean(IS_JOINED)!!,
                it.documents.first().getField<Int>(POINTS)!!,
                )
        }
        return user
    }
}