package cat.iesvidreres.tversus.src.ui.home.tabs.spend_points

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentFunctionsAdminBinding
import cat.iesvidreres.tversus.databinding.FragmentSpendPointsBinding
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.ShopCard
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.ShopCardProvider
import cat.iesvidreres.tversus.src.data.providers.SpendItemProvider
import cat.iesvidreres.tversus.src.data.providers.nodejs.userNode
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab.ShopRVAdapter
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class SpendPointsFragment : Fragment() {
    private val spendPointsViewModel: SpendPointsViewModel by viewModels()
    private lateinit var binding: FragmentSpendPointsBinding
    val userLiveData = MutableLiveData<User>()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSpendPointsBinding.inflate(inflater, container, false)
        userNode.getUserFromNode(profileViewModel.authenticationRepository.getCurrentUser().email.toString()) { user ->
            userLiveData.postValue(user)
        }
        initUI()
        return binding.root
    }

    fun initUI() {
        setRecyclerView()
    }

    private fun setRecyclerView() {

        val cardRecyclerview = binding.rvSpendPoints
        cardRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        cardRecyclerview.setHasFixedSize(true)

        val cardAdapter = SpendPointsRVAdapter()
        cardRecyclerview.adapter = cardAdapter
        cardAdapter.setListData(SpendItemProvider.getListItemsToSpend())

        cardAdapter.setItemListener(object : SpendPointsRVAdapter.OnItemClickListener {
            override fun onItemClick(shopCard: ShopCard) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Cuidado")
                builder.setMessage("Estas seguro de que quieres canjear este premio?")
                builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, id ->

                    userLiveData.observe(requireActivity()) { user ->
                        if(user.tokens > shopCard.valor){
                            val gson = GsonBuilder().setLenient().create()
                            val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                                .addConverterFactory(GsonConverterFactory.create(gson)).build()
                            val api = retrofit.create(userAPI::class.java)
                            var tokensAfterBuy = user.tokens - shopCard.price
                            val userBuy = User(user.username,user.email,user.password,user.borndate,tokensAfterBuy,user.tournament_id,user.image,user.isJoined,user.points)
                            api.updateUser(user.email,userBuy).enqueue(object : Callback<User> {
                                @SuppressLint("LogNotTimber")
                                override fun onResponse(call: Call<User>, response: Response<User>) {
                                    if (response.isSuccessful) {
                                        val user = response.body()!!

                                    }
                                }
                                @SuppressLint("LogNotTimber")
                                override fun onFailure(call: Call<User>, t: Throwable) {
                                }
                            })
                        }else{
                            toast("No tienes suficientes tokens")
                        }

                    }
                }
                )
                builder.setNegativeButton("No",null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }


        })
    }
}