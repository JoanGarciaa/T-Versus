package cat.iesvidreres.tversus.src.data.providers

import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.ShopCard
import org.checkerframework.checker.units.qual.g

class ShopCardProvider {
    companion object {
        fun getListInfoCard(): MutableList<ShopCard> {
            return mutableListOf(
                ShopCard("Puñado de tokens",10,R.drawable.logo,1000),
                ShopCard("Saco de tokens",20,R.drawable.logo,2500),
                ShopCard("Caja de tokens",50,R.drawable.logo,6000),
                ShopCard("Cofre de tokens",90,R.drawable.logo,12000)
            )
        }
    }
}