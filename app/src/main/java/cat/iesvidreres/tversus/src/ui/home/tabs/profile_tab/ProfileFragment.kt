package cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentProfileBinding
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import timber.log.Timber


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var imageReference = Firebase.storage.reference
    var currentFile: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)


        binding.btnEditar.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        init()
        return binding.root
    }


    private fun init() {
        changeVisibility()
        retrofit()
        loadNewImage()
    }


    private fun changeVisibility() {
        binding.tvMoreInfo.setOnClickListener {
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


    fun loadNewImage() {
        val imageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    it?.data?.data?.let { uri ->
                        currentFile = uri

                        binding.ivAvatarUser.setImageURI(uri)

                    }
                } else {
                    Timber.tag("Mal").i("Mal")
                }
            }

        binding.ivAvatarUser.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                imageLauncher.launch(it)
            }
        }
    }

    private fun retrofit() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val api = retrofit.create(userAPI::class.java);
        var user: User
        api.getUserByEmail(profileViewModel.authenticationRepository.getCurrentUserEmail().email.toString())
            .enqueue(object : Callback<User> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<User>, response: Response<User>
                ) {
                    user = response.body()!!
                    binding.inputEmailText.text =
                        Editable.Factory.getInstance().newEditable(user.email)
                    binding.tokensUser.text = user.tokens.toString() + " TOKENS"
                    binding.tvUsername.text = user.username
                    binding.inputBornDateText.text =
                        Editable.Factory.getInstance().newEditable(user.borndate)
                    binding.progressBar2.visibility = View.GONE
                    Picasso.get().load(user.image).into(binding.ivAvatarUser)
                    api.update(user.email,user).enqueue(object : Callback<User> {
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(
                            call: Call<User>, response: Response<User>
                        ) {

                            imageReference = FirebaseStorage.getInstance().reference.child("profile")
                            imageReference = imageReference.child(System.currentTimeMillis().toString())
                            currentFile?.let {
                                imageReference.putFile(it).addOnSuccessListener {
                                    imageReference.downloadUrl.addOnSuccessListener {
                                        currentFile
                                    }.addOnFailureListener {
                                    }
                                }
                            }


                            user = response.body()!!
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Log.i("Error", "$t")
                        }

                    })
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }
}