package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.User
import com.app.projecte.tversus.AdaptadorMatch
import javax.inject.Inject

class JoinedTournamentRVAdapter @Inject constructor() : RecyclerView.Adapter<JoinedTournamentRVAdapter.ItemPlayersHolder>() {

    private lateinit var listener: JoinedTournamentRVAdapter.OnItemClickListener
    private var listData = listOf<User>()

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

    fun setListData(data: List<User>) {
        listData = data
    }

    fun setItemListener(listener: JoinedTournamentRVAdapter.OnItemClickListener) {
        this.listener = listener
    }

    fun setOnItemClickListener(clickListener: JoinedTournamentRVAdapter.OnItemClickListener){
        listener=clickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinedTournamentRVAdapter.ItemPlayersHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player_card, parent, false)

        return ItemPlayersHolder(v)
    }

    override fun onBindViewHolder(holder: JoinedTournamentRVAdapter.ItemPlayersHolder, position: Int) {
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


    inner class ItemPlayersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: User) {
            val username = itemView.findViewById<TextView>(R.id.tvUsername)
            username.text = user.username
        }

    }
}