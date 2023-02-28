package cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.edit_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentEditProfileBinding
import cat.iesvidreres.tversus.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentEditProfileBinding.inflate(inflater,container,false)

        return binding.root
    }

}