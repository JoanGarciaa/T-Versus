package cat.iesvidreres.tversus.src.ui.auth.signin.model

class UserRegister(
    val nickname : String,
    val email : String,
    val password: String,
    val years : String
) {
    fun isNotEmpty() =
        nickname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && years.isNotEmpty()
}