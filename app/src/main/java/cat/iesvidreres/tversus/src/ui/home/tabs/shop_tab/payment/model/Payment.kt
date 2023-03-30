package cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab.payment.model

data class Payment(
    val cardNumber:String,
    val cvv: String,
    val caducity: String
){
    fun isNotEmpty() = cardNumber.isNotEmpty() && cvv.isNotEmpty() && caducity.isNotEmpty()
}
