package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds

import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.models.Equip
import cat.iesvidreres.tversus.src.data.models.Match
import cat.iesvidreres.tversus.src.data.models.Ronda

class ViewModelRondas: ViewModel() {
    var rondaActual: Ronda?=null
    var matchActual: Match?=null

    val equiplist = listOf(
        Equip("Equipo 1", listOf("Juan", "María", "Pedro", "Laura", "Carlos")),
        Equip("Equipo 2", listOf("Sofía", "Andrés", "Lucía", "Fernando", "Marta")),
        Equip("Equipo 3", listOf("Ana", "Roberto", "Julia", "Diego", "Elena")),
        Equip("Equipo 4", listOf("Isabel", "David", "Pablo", "Marina", "Jorge"))
    )

    val matchList = listOf(
        Match(equiplist[0], equiplist[1], null),
        Match(equiplist[2], equiplist[3], null),
        Match(equiplist[0], equiplist[3], null)
    )

    val rondaList = listOf(
        Ronda(1, "equiplist", equiplist)
    )

    fun setRonda(position: Int){
        rondaActual=rondaList[position]
    }

    fun setMatch(position: Int){
        matchActual=matchList[position]
    }

    fun nuevaRonda(ronda:Ronda){
        var ganadores = ronda.getGuanyadors()
        val myList = mutableListOf<Match>()
        var rondaNueva:Ronda = Ronda(ronda.roundNumber+1,"1",ganadores,"", emptyList())
        rondaNueva.crearMatches()
        //Añadir al array de rondas
    }
}