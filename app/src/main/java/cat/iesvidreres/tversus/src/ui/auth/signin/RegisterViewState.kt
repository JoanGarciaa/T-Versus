package cat.iesvidreres.tversus.src.ui.auth.signin

data class RegisterViewState(
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true,
    val isValidNickName: Boolean = true,
    val isValidBornDate: Boolean = true
) {
    fun userValidated() = isValidEmail && isValidPassword && isValidNickName && isValidBornDate

}