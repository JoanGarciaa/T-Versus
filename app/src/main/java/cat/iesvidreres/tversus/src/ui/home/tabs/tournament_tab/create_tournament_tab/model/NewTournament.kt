package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.model

data class NewTournament(
    var id : String,
    var image: Int,
    var name: String,
    var description: String,
    var price: String,
    var organizer : String,
    var teamsnumber: Int,
    var modality: String
    //TODO Poner lo que falta
){
    fun isNotEmpty() =name.isNotEmpty() && description.isNotEmpty()
}