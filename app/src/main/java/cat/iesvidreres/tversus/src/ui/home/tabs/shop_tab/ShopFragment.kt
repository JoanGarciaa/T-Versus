package cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.databinding.FragmentShopBinding
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.ShopCard
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.ShopCardProvider
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()

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

                val gson = GsonBuilder().setLenient().create()
                val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                    .addConverterFactory(GsonConverterFactory.create(gson)).build()
                val api = retrofit.create(userAPI::class.java);
                var user: User
                api.getUserByEmail(profileViewModel.authenticationRepository.getCurrentUserEmail().email.toString())
                    .enqueue(object : Callback<User> {
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(
                            call: Call<User>, response: Response<User>
                        ) {
                            user = response.body()!!
                            val oldTokens = user.tokens
                            val newTokens = shopCard.valor
                            val finalTokens = oldTokens + newTokens
                            user.tokens = finalTokens
                            api.buyTokens(profileViewModel.authenticationRepository.getCurrentUserEmail().email.toString(), user).enqueue(object : Callback<User> {
                                @SuppressLint("SetTextI18n")
                                override fun onResponse(
                                    call: Call<User>, response: Response<User>
                                ) {
                                    user = response.body()!!
                                }
                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    Log.i("Error", "$t")
                                }
                            })
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Log.i("Error", "$t")
                        }

                    })

            }
        })
    }


}
