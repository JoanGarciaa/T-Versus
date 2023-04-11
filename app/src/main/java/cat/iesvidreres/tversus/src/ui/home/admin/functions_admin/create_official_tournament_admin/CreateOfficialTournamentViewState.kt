package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.create_official_tournament_admin

data class CreateOfficialTournamentViewState(
    val isValidId: Boolean = true,
    val isValidName: Boolean = true,
    val isValidDescription: Boolean = true,
    val isValidPrice: Boolean = true,
) {
    fun tournamentOfficialValidated() = isValidId && isValidName && isValidDescription && isValidPrice
}