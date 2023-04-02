package cat.iesvidreres.tversus.src.ui.home.tabs.home_tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.Tournament
import javax.inject.Inject

class HomeRVAdapter @Inject constructor() : RecyclerView.Adapter<HomeRVAdapter.ItemTournamentHolder>() {


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTournamentHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tournament_view, parent, false)

        return ItemTournamentHolder(v)
    }

    override fun onBindViewHolder(holder: ItemTournamentHolder, position: Int) {
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

    inner class ItemTournamentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(tournament: Tournament) {
            val descriptionViewTournament = itemView.findViewById<TextView>(R.id.tvDescriptionViewTournament)
            descriptionViewTournament.text = tournament.description
            val imageViewTournament = itemView.findViewById<ImageView>(R.id.ivViewTournament)
            imageViewTournament.setImageResource(R.drawable.valotourn)
            val nameViewTournament = itemView.findViewById<TextView>(R.id.tvNameViewTournament)
            nameViewTournament.text = tournament.name
            val priceViewTournament = itemView.findViewById<TextView>(R.id.tvPriceTournament)
            priceViewTournament.text = tournament.price.toString()
            val btnJoin = itemView.findViewById<Button>(R.id.btnToJoin)

            itemView.setOnClickListener {
                listener.onItemClick(tournament)
            }

            btnJoin.setOnClickListener{
                listener.onItemClick(tournament)
            }



        }


    }
}
