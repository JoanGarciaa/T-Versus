package cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.edit_profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentEditProfileBinding
import cat.iesvidreres.tversus.databinding.FragmentProfileBinding
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.nodejs.userNode
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
import com.google.gson.GsonBuilder
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
    private val profileViewModel: ProfileViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        retrofit()
    }

    private fun retrofit() {
        val userLiveData = MutableLiveData<User>()
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(userAPI::class.java);

        userNode.getUserFromNode(profileViewModel.authenticationRepository.getCurrentUser().email.toString()) { user ->
            userLiveData.postValue(user)
        }

        userLiveData.observe(requireActivity()) { user ->
            binding.inputEmailEditText.text = Editable.Factory.getInstance().newEditable(user.email)
            binding.inputUsernameEditText.text =
                Editable.Factory.getInstance().newEditable(user.username)
            binding.inputBorndateEditText.text =
                Editable.Factory.getInstance().newEditable(user.borndate)
            binding.btnModifyUser.setOnClickListener {

                val updatedEmail = binding.inputEmailEditText.text.toString()
                val updatedUsername = binding.inputUsernameEditText.text.toString()
                val updatedBorndate = binding.inputBorndateEditText.text.toString()
                var updateUser = User(
                    updatedUsername,
                    updatedEmail,
                    user.password,
                    updatedBorndate,
                    user.tokens,
                    user.tournament_id,
                    user.image,
                    user.isJoined,
                    user.points
                )


                api.updateUser(user.email, updateUser).enqueue(object : Callback<User> {
                    override fun onResponse(
                        call: Call<User>, response: Response<User>
                    ) {
                        updateUser = response.body()!!
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.i("Erroddr", "$t")
                    }
                })
                toast("Has modificado correctamente tus datos")
                view?.findNavController()
                    ?.navigate(R.id.action_editProfileFragment_to_profileFragment)
            }
            binding.btnDeleteAccount.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("ELIMINAR CUENTA!")
                builder.setMessage("Estas seguro de que quieres eliminar tu cuenta de forma permanente?")
                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener { dialog, id ->
                        api.deleteUser(user.email).enqueue(object : Callback<User> {
                            override fun onResponse(
                                call: Call<User>, response: Response<User>
                            ) {
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Log.i("Erroddr", "$t")
                            }
                        })
                    })
                builder.setNegativeButton("No",null)
                builder.show()
            }


        }
    }

}