package cat.iesvidreres.tversus.src.data.providers

import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.ShopCard

class SpendItemProvider {
    companion object {
        fun getListItemsToSpend(): MutableList<ShopCard> {
            return mutableListOf(
                ShopCard("Iphone 11 pro",200000, R.drawable.iphone11pro,200000),
                ShopCard("Pelota de futbol",2500, R.drawable.pelota,2500),
                ShopCard("Mac Air m1 2020",6000, R.drawable.macair,6000),
                ShopCard("Google play 10€",12000, R.drawable.googleplay,12000)
            )
        }
    }
}
