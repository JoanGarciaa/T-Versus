package cat.iesvidreres.tversus.src.ui.home.admin.functions_admin.list_tournaments_admin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.Tournament
import javax.inject.Inject

class ListTournamentsRVAdapter @Inject constructor() : RecyclerView.Adapter<ListTournamentsRVAdapter.ItemTournamentAdminHolder>() {
    private lateinit var listener: OnItemClickListener
    private var listData = listOf<Tournament>()

    interface OnItemClickListener {
        fun onItemClick(tournament: Tournament)
    }

    fun setListData(data: List<Tournament>) {
        listData = data
    }

    fun setItemListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTournamentAdminHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_tournament, parent, false)

        return ItemTournamentAdminHolder(v)
    }

    override fun onBindViewHolder(holder: ItemTournamentAdminHolder, position: Int) {
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

    inner class ItemTournamentAdminHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(tournament: Tournament) {
            Log.i("bonndia","${tournament.id}")
            val idViewTournament = itemView.findViewById<TextView>(R.id.tvIdTournamentAdmin)
            idViewTournament.text = tournament.id
            val nameViewTournament = itemView.findViewById<TextView>(R.id.tvNameTournamentAdmin)
            nameViewTournament.text = tournament.name
            val emailViewTournament = itemView.findViewById<TextView>(R.id.tvEmailTournamentAdmin)
            emailViewTournament.text = tournament.organizer
            val priceViewTournament = itemView.findViewById<TextView>(R.id.tvPriceTournamentAdmin)
            priceViewTournament.text = tournament.price.toString()
            val typeViewTournament = itemView.findViewById<TextView>(R.id.tvTypeTournamentAdmin)
            typeViewTournament.text = tournament.type

            itemView.setOnClickListener {
                listener.onItemClick(tournament)
            }
        }


    }
}