package cat.iesvidreres.tversus.src.data.models


data class User(
    val username : String,
    val email: String,
    val password: String,
    val borndate : String,
    val tokens: Int
)