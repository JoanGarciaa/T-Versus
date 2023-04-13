package cat.iesvidreres.tversus.src.ui.home.admin.login_admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentLoginAdminBinding
import cat.iesvidreres.tversus.src.core.ex.toast

class LoginAdminFragment : Fragment() {
    private lateinit var binding: FragmentLoginAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentLoginAdminBinding.inflate(inflater,container,false)

        succesLogin()
        return binding.root
    }

    private fun succesLogin(){
        val username = binding.inputUsernameAdminText.text.toString()
        val password = binding.inputPasswordAdminText.text.toString()
        Log.e("$username","$password")
        binding.buttonLoginAdmin.setOnClickListener {
            if(username == "AdminTversus" && password == "admin123"){
                view?.findNavController()?.navigate(R.id.action_loginAdminFragment_to_functionsAdminFragment)
                toast("Bienvenido admin")

            }else{
                toast("Estas no son las credenciales de un admin")
            }
        }
    }

}