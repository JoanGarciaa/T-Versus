package cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.edit_profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentEditProfileBinding
import cat.iesvidreres.tversus.databinding.FragmentProfileBinding
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val editProfileViewModel: EditProfileViewModel by viewModels()
    private val profileViewModel : ProfileViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentEditProfileBinding.inflate(inflater,container,false)
        initUI()
        return binding.root
    }

    private fun initUI(){
        retrofit()
    }

    private fun retrofit() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val api = retrofit.create(userAPI::class.java);
        var user: User
        api.getUserByEmail(profileViewModel.authenticationRepository.getCurrentUserEmail().email.toString()).enqueue(object :
            Callback<User> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                user = response.body()!!
                binding.inputEmailEditText.text =  Editable.Factory.getInstance().newEditable(user.email)
                binding.inputUsernameEditText.text = Editable.Factory.getInstance().newEditable(user.username)
                binding.inputBorndateEditText.text = Editable.Factory.getInstance().newEditable(user.borndate)
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.i("Error","$t")
            }

        })
    }

}