package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentFunctionsAdminBinding

class FunctionsAdminFragment : Fragment() {

    private lateinit var binding: FragmentFunctionsAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentFunctionsAdminBinding.inflate(inflater,container,false)
        goTo()
        return binding.root
    }

    fun goTo(){
        with(binding){
            btnToListUsers.setOnClickListener {view?.findNavController()?.navigate(R.id.action_functionsAdminFragment_to_listUsersFragmentAdmin) }
            btnToCreateOfficialTournament.setOnClickListener {view?.findNavController()?.navigate(R.id.action_functionsAdminFragment_to_createOfficialTournamentFragment) }
            btnToListTournaments.setOnClickListener {view?.findNavController()?.navigate(R.id.action_functionsAdminFragment_to_listTournamentsAdminFragment) }
        }
    }

}