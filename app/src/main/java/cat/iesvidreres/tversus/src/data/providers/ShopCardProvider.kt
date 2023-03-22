package cat.iesvidreres.tversus.src.data.providers

import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.ShopCard

class ShopCardProvider {
    companion object {
        fun getListInfoCard(): MutableList<ShopCard> {
            return mutableListOf(
                ShopCard("Puñado de tokens",10,R.drawable.ttokens,1000),
                ShopCard("Saco de tokens",20,R.drawable.ttokens,2500),
                ShopCard("Caja de tokens",50,R.drawable.ttokens,6000),
                ShopCard("Cofre de tokens",90,R.drawable.ttokens,12000)
            )
        }
    }
}