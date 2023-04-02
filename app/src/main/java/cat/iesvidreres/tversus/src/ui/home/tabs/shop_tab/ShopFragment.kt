package cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentShopBinding
import cat.iesvidreres.tversus.src.data.models.ShopCard
import cat.iesvidreres.tversus.src.data.providers.ShopCardProvider
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val paymentViewModel : PaymentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)

        setRecyclerView()

        return binding.root
    }

    private fun setRecyclerView() {

        val cardRecyclerview = binding.recyclerViewShop
        cardRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        cardRecyclerview.setHasFixedSize(true)

        val cardAdapter = ShopRVAdapter()
        cardRecyclerview.adapter = cardAdapter
        cardAdapter.setListData(ShopCardProvider.getListInfoCard())

        cardAdapter.setItemListener(object : ShopRVAdapter.OnItemClickListener {
            override fun onItemClick(shopCard: ShopCard) {
                paymentViewModel.setItemShop(shopCard)
                Log.i("adasdasdsadasd","$shopCard")
                view?.findNavController()?.navigate(R.id.action_shopFragment_to_paymentFragment)
            }
        })
    }


}
