package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentFunctionsAdminBinding
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.data.providers.firebase.UserRepository
import cat.iesvidreres.tversus.src.data.providers.nodejs.userNode
import cat.iesvidreres.tversus.src.data.providers.nodejs.userNode.getUserFromNode
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
import org.w3c.dom.Node

class FunctionsAdminFragment : Fragment() {

    private lateinit var binding: FragmentFunctionsAdminBinding
    @SuppressLint("LogNotTimber")
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