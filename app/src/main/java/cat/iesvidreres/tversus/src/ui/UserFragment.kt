package cat.iesvidreres.tversus.src.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentMenuBinding
import cat.iesvidreres.tversus.databinding.FragmentUserBinding


class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate<FragmentUserBinding>(inflater, R.layout.fragment_menu,container,false);

        return binding.root
    }

}