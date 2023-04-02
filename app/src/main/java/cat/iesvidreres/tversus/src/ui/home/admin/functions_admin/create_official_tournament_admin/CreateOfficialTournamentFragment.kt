package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.create_official_tournament_admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.iesvidreres.tversus.databinding.FragmentCreateOfficialTournamentBinding


class CreateOfficialTournamentFragment : Fragment() {
    private lateinit var binding: FragmentCreateOfficialTournamentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentCreateOfficialTournamentBinding.inflate(inflater,container,false)

        return binding.root
    }

}