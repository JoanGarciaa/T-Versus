package cat.iesvidreres.tversus.src.ui.home.tabs.spend_points

import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.src.data.models.ShopCard
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpendPointsViewModel @Inject constructor(): ViewModel() {

    private var _shopCard: ShopCard? = null
    val shopCard get() = _shopCard

    fun setItemShop(shopCard: ShopCard) {
        _shopCard = shopCard
    }
}