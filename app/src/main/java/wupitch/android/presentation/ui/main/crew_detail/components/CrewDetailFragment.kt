package wupitch.android.presentation.ui.main.crew_detail.components

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

class CrewDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("crewId")?.let { crewId ->
//            viewModel.onTriggerEvent(GetRecipeEvent(recipeId))
            //todo : get crew from viewModel with the id.
            Log.d("{CrewDetailFragment.onCreate}", crewId.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply { 
            setContent {
                Text(text = "crew detail!")
            }
        }
    }
}