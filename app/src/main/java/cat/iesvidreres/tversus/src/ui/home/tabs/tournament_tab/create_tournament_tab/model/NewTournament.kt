package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.model

data class NewTournament(
    var image: Int,
    var name: String,
    var description: String,
    var price: Int,
){
    fun isNotEmpty(){
        name.isNotEmpty() && description.isNotEmpty()
    }
}