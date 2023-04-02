package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.list_users_admin


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.User
import javax.inject.Inject

class ListUsersRVAdapter @Inject constructor() : RecyclerView.Adapter<ListUsersRVAdapter.ItemUserAdminHolder>(){
    private lateinit var listener: OnItemClickListener
    private var listData = listOf<User>()

    interface OnItemClickListener {
        fun onItemClick(user: User)
        fun onDeleteUser(user:User)
    }


    fun setListData(data: List<User>) {
        listData = data
    }

    fun setItemListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemUserAdminHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_user, parent, false)

        return ItemUserAdminHolder(v)
    }

    override fun onBindViewHolder(holder: ItemUserAdminHolder, position: Int) {
        val shopCard = listData[position]
        holder.bindView(shopCard)
    }

    override fun getItemCount(): Int {
        return if (listData.size > 0) {
            listData.size
        } else {
            0
        }
    }



    inner class ItemUserAdminHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: User) {

            val usernameView = itemView.findViewById<TextView>(R.id.tvUsernameAdmin)
            usernameView.text = user.username
            val emailView = itemView.findViewById<TextView>(R.id.tvEmailAdmin)
            emailView.text = user.email
            val emailViewTournament = itemView.findViewById<TextView>(R.id.tvBorndateAdmin)
            emailViewTournament.text = user.borndate
            val btnDelete = itemView.findViewById<ImageView>(R.id.ivDeleteUser)

            itemView.setOnClickListener {
                listener.onItemClick(user)
            }

            btnDelete.setOnClickListener{
                listener.onDeleteUser(user)
            }


        }


    }
}