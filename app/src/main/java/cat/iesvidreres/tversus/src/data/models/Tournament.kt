package cat.iesvidreres.tversus.src.data.models

import cat.iesvidreres.tversus.R
import com.google.common.math.IntMath
import com.google.firebase.firestore.Exclude
import java.io.Serializable
import java.util.*

data class Tournament (
    val name : String,
    val id : String,
    val game : String,
    val organizer : String,
    val description : String,
    val price: Int,
    val image: Int,
    val type : String,
    var rounds: List<Ronda> = mutableListOf(),
    @Exclude var users: List<Map<*, *>> = emptyList(),
    val teamsNumber: Int,
    val reward: Double,
    var actualRound: Int? = 0,
    var modality: String? = ""

): Serializable {

    //Funcion que genera id automaticamente para rellenar los participantes del torneo
    fun getUsers(){
        val lista: MutableList<Map<*, *>> = mutableListOf()
        val limite: Int = (teamsNumber*5)

        for (i in 1..limite) {
            val name = "Nombre $i"
            val map = mutableMapOf<String, Any>()
            map["nom"] = name
            map["correu"] = name+"@institutvidreres.cat"
            lista.add(map)
        }

        this.users=lista
    }

    fun newRound() {
        var lista= actualRound?.let { rounds.get(it).getGuanyadors() }?:""
        var numero= actualRound?.plus(1)?:0
        var ronda:Ronda= Ronda(numero,id, lista as List<Equip>,"Abierta", emptyList())
        ronda.crearMatches()
        this.rounds= (rounds+ronda) as MutableList<Ronda>
    }

}

