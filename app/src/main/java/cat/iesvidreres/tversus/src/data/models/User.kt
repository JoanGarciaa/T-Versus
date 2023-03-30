package cat.iesvidreres.tversus.src.data.models


data class User(
    val username : String,
    val email: String,
    val password: String,
    val borndate : String,
    var tokens: Int,
    var tournament_id: String
)