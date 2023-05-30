package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasCreador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.Ronda
import com.app.projecte.tversus.AdaptadorMatch


import javax.inject.Inject

class AdminRoundsAdapter @Inject constructor() : RecyclerView.Adapter<AdminRoundsAdapter.AdminRoundsHolder>() {


    private var listData = listOf<Ronda>()
    private lateinit var itemListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        itemListener=clickListener
    }

    fun get (position: Int): Ronda {
         return listData.get(position)
    }
    fun setListData(data: List<Ronda>) {
        listData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminRoundsHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ronda_layout, parent, false)
        return AdminRoundsHolder(itemView,itemListener)

    }

    override fun onBindViewHolder(holder: AdminRoundsHolder, position: Int) {
        val ronda = listData[position]
        holder.bindView(ronda)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class AdminRoundsHolder(itemView: View,clickListener: AdminRoundsAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        init{
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }

        fun bindView(ronda: Ronda) {
            var estadoRonda: TextView = itemView.findViewById(R.id.textViewEstadoRonda)
            estadoRonda.text=ronda.estadoRonda


            var rondaTextView: TextView = itemView.findViewById(R.id.rondaTextView)
            rondaTextView.text="Ronda "+ronda.roundNumber

            var equiposParticipantes: TextView = itemView.findViewById(R.id.textNumeroEquipos)
            if (ronda.teams!=null){
                equiposParticipantes.text=ronda.teams.size.toString()
            }


        }

    }
}