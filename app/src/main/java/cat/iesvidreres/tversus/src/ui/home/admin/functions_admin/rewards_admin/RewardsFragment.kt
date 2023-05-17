package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.rewards_admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cat.iesvidreres.tversus.databinding.FragmentRewardsBinding
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RewardsFragment : Fragment() {
    private lateinit var binding: FragmentRewardsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRewardsBinding.inflate(inflater, container, false)


        initUI()
        return binding.root
    }

    private fun initUI() {
        sendReward()
    }

    fun sendReward() {
        getUsers()

    }

    fun getUsers() {

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/").addConverterFactory(GsonConverterFactory.create(gson)).build()
        lateinit var users: MutableList<User>
        val api = retrofit.create(userAPI::class.java);

        api.getUsers().enqueue(object : Callback<MutableList<User>> {
            override fun onResponse(
                call: Call<MutableList<User>>, response: Response<MutableList<User>>,
            ) {
                users = response.body()!!


                binding.bntSendRewards.setOnClickListener {
                    var winnerPlayer = binding.editTextUsernameReward.text.toString()
                    var userFound = false;
                    lateinit var userReward :User
                    for (username in users) {
                        if (winnerPlayer == username.username) {
                            userFound = true
                            userReward = username
                            break
                        } else {
                            userFound = false
                        }
                    }
                    if (userFound == true) {
                        toast("Usuario encontrado")
                        if(binding.etReward.text.toString() == "" || binding.etReward.text.toString() == ""){
                            toast("Vas a enviar 0 tokens?")
                        }else if(binding.etReward.text.toString().equals(binding.etReward2.text.toString())){
                            sendTokens(userReward)
                        }else{
                            toast("Los campos de tokens no coinciden!")

                        }

                    }
                }

            }

            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                Log.i("Erroddr", "$t")
            }

        })

    }
    fun sendTokens(userReward:User){
        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/").addConverterFactory(GsonConverterFactory.create(gson)).build()
        lateinit var users: MutableList<User>
        val api = retrofit.create(userAPI::class.java);
        var userNew = userReward
        val finalTokens = userReward.tokens + binding.etReward2.text.toString().toInt()
        userNew = User(userReward.username,userReward.email,userReward.password,userReward.borndate,finalTokens,userReward.tournament_id,userReward.image,userReward.isJoined,userReward.points)
        api.updateUser(userReward.email,userNew).enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>,
            ) {

            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }
        })

    }

}