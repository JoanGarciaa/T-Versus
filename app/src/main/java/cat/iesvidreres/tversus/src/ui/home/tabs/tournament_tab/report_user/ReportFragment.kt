package cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.report_user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentReportBinding
import cat.iesvidreres.tversus.databinding.FragmentTournamentBinding
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.info_tournament.InfoTournamentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReportFragment : Fragment() {
    private lateinit var binding : FragmentReportBinding
    private val reportViewModel: ReportViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentReportBinding.inflate(inflater,container,false)
        binding.btnReport.setOnClickListener {
            Toast.makeText(requireContext(), "Esta función aún no está implementada", Toast.LENGTH_SHORT).show()
        }
        initUI()

        return binding.root
    }

    fun initUI(){
        bundleInfo()
//        sharedViewModelInfo()
    }

    fun sharedViewModelInfo(){
        with(binding){
            reportViewModel.viewModelScope.launch{
                reportViewModel.username.observeForever(){
                    tvShowName.text = it
                }
                reportViewModel.tournamentId.observeForever(){
                    tvShowIdT.text = it
                }
            }
        }
    }

    fun bundleInfo(){
        with(binding){
            tvShowName.text = arguments?.getString("username")
            tvShowIdT.text = arguments?.getString("idTournament")
        }
    }





}