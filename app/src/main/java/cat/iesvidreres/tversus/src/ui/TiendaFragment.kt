package cat.iesvidreres.tversus.src.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentMenuBinding

class TiendaFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate<FragmentMenuBinding>(inflater, R.layout.fragment_menu,container,false);

        return binding.root
    }

}