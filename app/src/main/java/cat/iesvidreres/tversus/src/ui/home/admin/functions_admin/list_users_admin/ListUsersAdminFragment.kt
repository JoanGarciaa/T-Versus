package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.list_users_admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cat.iesvidreres.tversus.databinding.FragmentListUsersAdminBinding
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.User
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListUsersAdminFragment : Fragment() {
    private lateinit var binding: FragmentListUsersAdminBinding
    private var cardAdapter = ListUsersRVAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListUsersAdminBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        setRecyclerView()

    }

    private fun setRecyclerView() {

        val cardRecyclerview = binding.rvUsersAdmin
        cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        cardRecyclerview.setHasFixedSize(true)
        cardRecyclerview.adapter = cardAdapter

        cardAdapter.setItemListener(object : ListUsersRVAdapter.OnItemClickListener {
            override fun onItemClick(user: User) {

            }

            override fun onDeleteUser(user: User) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Cuidado!")
                builder.setMessage("Estas seguro de que quieres eliminar al usuario?")
                builder.setPositiveButton(
                    "Si, borrar!",
                    DialogInterface.OnClickListener { dialog, id ->
                        val gson = GsonBuilder().setLenient().create()
                        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                            .addConverterFactory(GsonConverterFactory.create(gson)).build()
                        val api = retrofit.create(userAPI::class.java)
                        var thisUser  = User(user.username,user.email,user.password,user.email,user.tokens,"",user.image,user.isJoined)
                        api.deleteUser(user.email).enqueue(object : Callback<User> {
                            override fun onResponse(
                                call: Call<User>, response: Response<User>
                            ) {
                                if (response.body() != null) {
                                    thisUser = response.body()!!
                                    observeCard()
                                    cardAdapter.notifyDataSetChanged()
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Log.i("Error delete", "$t")
                                observeCard()
                            }
                        })
                    })
                builder.setNegativeButton("No", null)
                val dialog = builder.create()
                dialog.show()
            }
        })
        observeCard()
    }

    private fun observeCard() {
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(userAPI::class.java)
        var userList: MutableList<User>
        api.getUsers().enqueue(object : Callback<MutableList<User>> {
            override fun onResponse(
                call: Call<MutableList<User>>, response: Response<MutableList<User>>
            ) {
                userList = response.body()!!
                cardAdapter.setListData(userList)

                cardAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                Log.i("Erroddr", "$t")
            }


        })

    }

}