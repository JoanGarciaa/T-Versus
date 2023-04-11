package cat.iesvidreres.tversus.src.data.models

import cat.iesvidreres.tversus.R


data class User(
    val username : String,
    val email: String,
    val password: String,
    val borndate : String,
    var tokens: Int,
    var tournament_id: String,
    var image: String,
    var isJoined : Boolean = false
)