package cat.iesvidreres.tversus.src.ui.home.tabs.spend_points

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.ShopCard
import cat.iesvidreres.tversus.src.data.models.Tournament
import javax.inject.Inject

class SpendPointsRVAdapter @Inject constructor() : RecyclerView.Adapter<SpendPointsRVAdapter.ItemSpendPointsHolder>() {


    private lateinit var listener: OnItemClickListener
    private var listData = listOf<ShopCard>()

    interface OnItemClickListener {
        fun onItemClick(shopCard: ShopCard)
    }

    fun setListData(data: List<ShopCard>) {
        listData = data
    }

    fun setItemListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSpendPointsHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spend_points, parent, false)

        return ItemSpendPointsHolder(v)
    }

    override fun onBindViewHolder(holder: ItemSpendPointsHolder, position: Int) {
        val itemCard = listData[position]
        holder.bindView(itemCard)
    }

    override fun getItemCount(): Int {
        return if (listData.size > 0) {
            listData.size
        } else {
            0
        }
    }


    inner class ItemSpendPointsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(card: ShopCard) {
            val nameViewSpendItem = itemView.findViewById<TextView>(R.id.tvNameViewTournament)
            nameViewSpendItem.text = card.name
            val imageViewSpendItem = itemView.findViewById<ImageView>(R.id.ivViewTournament)
            imageViewSpendItem.setImageResource(card.img)
            val priceViewSpendItem = itemView.findViewById<TextView>(R.id.tvPriceTournament)
            priceViewSpendItem.text = card.price.toString()

            itemView.setOnClickListener{
                listener.onItemClick(card)
            }



        }


    }
}