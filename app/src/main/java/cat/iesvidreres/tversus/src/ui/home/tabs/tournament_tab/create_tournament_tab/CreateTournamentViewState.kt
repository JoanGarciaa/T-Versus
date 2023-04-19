package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab

data class CreateTournamentViewState(
    val isValidName:Boolean = true,
    val isValidDescription: Boolean = true
)
{
    fun createTournamentValidated() = isValidName && isValidDescription
}