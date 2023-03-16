package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab

data class CreateTournamentViewState(
    val isValidName:Boolean = false,
    val isValidDescription: Boolean = false,
)
{
    fun createTournamentValidated() = isValidName && isValidDescription
}