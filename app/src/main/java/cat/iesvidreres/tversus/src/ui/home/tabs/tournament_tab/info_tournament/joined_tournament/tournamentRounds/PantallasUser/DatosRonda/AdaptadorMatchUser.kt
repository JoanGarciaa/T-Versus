package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.joined_tournament.tournamentRounds.PantallasUser.DatosRonda

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.data.models.Match
import com.app.projecte.tversus.AdaptadorMatch

class AdaptadorMatchUser(var listaObjetos: List<Match>) : RecyclerView.Adapter<ViewHolder>() {

    private lateinit var itemListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setTaskList(newList: List<Match>?) {
        if (newList != null) {
            listaObjetos = newList
        }
        Log.d("TaskList Adaptadormatch","Actualizada")
        Log.d("TaskList Adaptadormatch",newList.toString())
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        itemListener=clickListener
    }
    fun get (position: Int): Match {
        return listaObjetos.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.match_layout, parent, false)
        return ViewHolder(itemView, itemListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = listaObjetos[position]
        holder.equip1.text= current.equipo1.nom
        holder.equip2.text= current.equipo2.nom

        if (current.ganador?.nom.equals("null")){
            holder.ganador.text=""
        }else{
            holder.ganador.text="Ganador:"+current.ganador?.nom
        }


    }

    override fun getItemCount(): Int {
        return listaObjetos.size
    }

}



class ViewHolder(val itemView: View, clickListener: AdaptadorMatchUser.onItemClickListener) : RecyclerView.ViewHolder(itemView) {
    // val ronda : TextView = itemView.findViewById(R.id.rondaTextView)
    val equip1 : TextView = itemView.findViewById(R.id.textViewEquip1)
    val equip2 : TextView = itemView.findViewById(R.id.textViewEquip2)
    val ganador : TextView = itemView.findViewById(R.id.textViewWinner)
    init{
        itemView.setOnClickListener{
            clickListener.onItemClick(adapterPosition)
        }
    }


}