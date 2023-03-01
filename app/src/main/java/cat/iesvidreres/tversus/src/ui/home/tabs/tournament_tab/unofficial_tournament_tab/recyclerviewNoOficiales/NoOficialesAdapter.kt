package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.unofficial_tournament_tab.recyclerviewNoOficiales

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.databinding.TorneoItemBinding
import cat.iesvidreres.tversus.src.data.models.Tournament

class NoOficialesAdapter (private val cardDataList: List<Tournament>) : RecyclerView.Adapter<NoOficialesHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoOficialesHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TorneoItemBinding.inflate(inflater, parent, false)
            return NoOficialesHolder(binding)
        }

        override fun onBindViewHolder(holder: NoOficialesHolder, position: Int) {
            val cardData = cardDataList[position]
            holder.bind(cardData)
        }

        override fun getItemCount() = cardDataList.size
    }





