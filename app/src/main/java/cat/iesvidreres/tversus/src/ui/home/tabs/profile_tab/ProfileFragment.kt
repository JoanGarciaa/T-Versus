package cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentProfileBinding
import cat.iesvidreres.tversus.databinding.FragmentShopBinding
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentProfileBinding.inflate(inflater,container,false)


        init()
        return binding.root
    }


    private fun init(){
        changeVisibility()
        ///getFields()
        retrofit()
    }


    private fun changeVisibility(){
        binding.tvModificar.setOnClickListener {
            if (binding.inputEmail.isVisible && binding.inputBornDate.isVisible) {
                binding.inputEmail.setVisibility(View.GONE)
                binding.inputBornDate.setVisibility(View.GONE)
            } else {
                binding.inputEmail.setVisibility(View.VISIBLE)
                binding.inputBornDate.setVisibility(View.VISIBLE)
            }
        }

        binding.imageViewArrow.setOnClickListener {
            if (binding.inputEmail.isVisible && binding.inputBornDate.isVisible) {
                binding.inputEmail.setVisibility(View.GONE)
                binding.inputBornDate.setVisibility(View.GONE)
            } else {
                binding.inputEmail.setVisibility(View.VISIBLE)
                binding.inputBornDate.setVisibility(View.VISIBLE)
            }
        }
    }

    private fun retrofit() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val api = retrofit.create(userAPI::class.java);
        var user:User
        api.getUserByEmail(profileViewModel.authenticationRepository.getCurrentUserEmail().email.toString()).enqueue(object : Callback<User> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                user = response.body()!!
                binding.inputEmailText.text =  Editable.Factory.getInstance().newEditable(user.email)
                binding.tokensUser.text = user.tokens.toString() + " TOKENS"
                binding.tvUsername.text = user.username
                binding.inputBornDateText.text = Editable.Factory.getInstance().newEditable(user.borndate)
                binding.progressBar2.visibility = View.GONE
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.i("Error","$t")
            }

        })
    }




}