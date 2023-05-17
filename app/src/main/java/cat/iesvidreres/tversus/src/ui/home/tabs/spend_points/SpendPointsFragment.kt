package cat.iesvidreres.tversus.src.ui.home.tabs.spend_points

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentFunctionsAdminBinding
import cat.iesvidreres.tversus.databinding.FragmentSpendPointsBinding
import cat.iesvidreres.tversus.src.data.models.ShopCard
import cat.iesvidreres.tversus.src.data.providers.ShopCardProvider
import cat.iesvidreres.tversus.src.data.providers.SpendItemProvider
import cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab.ShopRVAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpendPointsFragment : Fragment() {
    private val spendPointsViewModel : SpendPointsViewModel by viewModels()
    private lateinit var binding: FragmentSpendPointsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSpendPointsBinding.inflate(inflater, container, false)

        initUI()
        return binding.root
    }

    fun initUI(){
        setRecyclerView()
    }

    private fun setRecyclerView() {

        val cardRecyclerview = binding.rvSpendPoints
        cardRecyclerview.layoutManager = GridLayoutManager(requireContext(),2)
        cardRecyclerview.setHasFixedSize(true)

        val cardAdapter = SpendPointsRVAdapter()
        cardRecyclerview.adapter = cardAdapter
        cardAdapter.setListData(SpendItemProvider.getListItemsToSpend())

        cardAdapter.setItemListener(object : SpendPointsRVAdapter.OnItemClickListener {
            override fun onItemClick() {

            }
        })
    }

}