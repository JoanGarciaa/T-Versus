package cat.iesvidreres.tversus.src.data.models

import android.util.Log
import androidx.room.Ignore
import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Ronda(
    var roundNumber: Int,
    val tournamentId: String,
    var teams: List<Equip> = listOf<Equip>(),
    var estadoRonda:String = "Abierta",
    var matches: List<Match> = listOf<Match>()): Serializable {

    fun crearMatches() {
        val newMatches = mutableListOf<Match>()

        for (i in teams.indices step 2) {
            val a = teams[i]
            val b = teams[i+1]
            val ganador = Equip("null",listOf(null,null,null,null,null))
            val match = Match(a, b, ganador)
            newMatches.add(match)
        }

        matches = newMatches.toList()
    }

    fun getGuanyadors(): List<Equip> {
        val winners = mutableListOf<Equip>()

        if (matches != null) {
            for (i in matches.indices step 1) {
                val ganador: Equip? = matches[i].ganador
                ganador?.let { winners.add(it) }
            }
        }else{
            Log.d("Nulo","Es null")}

        return winners.toList()
    }

    fun cerrarRonda(){
        estadoRonda="Cerrada"
    }

}
