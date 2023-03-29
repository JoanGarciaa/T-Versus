package cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.ShopCard
import javax.inject.Inject

class ShopRVAdapter @Inject constructor() : RecyclerView.Adapter<ShopRVAdapter.ShopCardHolder>(){

    private lateinit var listener: OnItemClickListener
    private var listData = listOf<ShopCard>()

    interface OnItemClickListener {
        fun onItemClick(shopCard: ShopCard)
    }

    fun setListData(data:MutableList<ShopCard>){
        listData = data
    }

    fun setItemListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCardHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.shop_card_item, parent, false)

        return ShopCardHolder(v)
    }

    override fun onBindViewHolder(holder: ShopCardHolder, position: Int) {
        val shopCard = listData[position]
        holder.bindView(shopCard)
    }

    override fun getItemCount(): Int {
        return if(listData.size > 0){
            listData.size
        }else{
            0
        }
    }

    inner class ShopCardHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(shopCard: ShopCard) {

            val nameItemShop = itemView.findViewById<TextView>(R.id.nameShopItem)
            nameItemShop.text = shopCard.name
            val imgItemShop = itemView.findViewById<ImageView>(R.id.ivShopItem)
            imgItemShop.setImageResource(shopCard.img)
            val valorItemShop = itemView.findViewById<TextView>(R.id.valorItemShop)
            valorItemShop.text = shopCard.valor.toString()
            val priceItemShop = itemView.findViewById<TextView>(R.id.priceItemShop)
            priceItemShop.text = "${shopCard.price.toString() + "€"}"
            val btnComprar = itemView.findViewById<TextView>(R.id.btnBuyTokens)

//            itemView.setOnClickListener {
//                listener.onItemClick(shopCard)
//            }
            btnComprar.setOnClickListener{
                listener.onItemClick(shopCard)
            }

        }


    }
}