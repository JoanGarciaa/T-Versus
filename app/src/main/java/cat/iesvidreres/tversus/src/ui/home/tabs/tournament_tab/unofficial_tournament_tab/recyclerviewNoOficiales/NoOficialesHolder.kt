package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.unofficial_tournament_tab.recyclerviewNoOficiales

import androidx.recyclerview.widget.RecyclerView
import cat.iesvidreres.tversus.databinding.TorneoItemBinding
import cat.iesvidreres.tversus.src.data.models.Tournament

public class NoOficialesHolder (private val binding: TorneoItemBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(torneo: Tournament) {
        binding.tituloTorneo.text = torneo.title
        //binding.imageViewThumbnail.setImageResource(cardData.thumbnail)
    }
}