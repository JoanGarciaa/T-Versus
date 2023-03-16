package cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentProfileBinding
import cat.iesvidreres.tversus.databinding.FragmentShopBinding
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentProfileBinding.inflate(inflater,container,false)

        binding.btnEditar.setOnClickListener(){
            view?.findNavController()?.navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        init()

        return binding.root
    }


    private fun init(){
        changeVisibility()
        getFields()
    }

    private fun getFields(){
        profileViewModel.getUserFromDB().observe(requireActivity()){
            binding.inputEmailText.text = Editable.Factory.getInstance().newEditable(it.email)
            binding.inputBornDateText.text = Editable.Factory.getInstance().newEditable(it.borndate)
            binding.tvUsername.text = it.username
            binding.tokensUser.text = it.tokens
        }
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

}