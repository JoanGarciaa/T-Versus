package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.official_tournament_tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.iesvidreres.tversus.R

class OfficialTournamentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_official_tournament, container, false)
    }

}