package cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab.payment

data class PaymentViewState(
    val isValidCardNumber:Boolean = false,
    val isValidCardCvv: Boolean = false,
    val isValidCardCaducity : Boolean = false
)
{
    fun paymentValidated() = isValidCardNumber && isValidCardCaducity && isValidCardCvv
}