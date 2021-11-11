package wupitch.android.presentation.ui.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import wupitch.android.domain.model.CrewCardInfo

@Composable
fun CrewList(
    loading : Boolean,
    crewList : List<CrewCardInfo>,
    navigationToCrewDetailScreen : (Int) -> Unit
){
    Box(modifier = Modifier.fillMaxSize().background(Color.White)){
        if(loading && crewList.isEmpty()) {
            //todo : show progressbar
        } else if(crewList.isEmpty()) {
            //todo : show no crew.
        } else {
            LazyColumn {
                itemsIndexed(
                    items = crewList
                ){ index, crew ->
                    //todo : get next page.
                    CrewCard(crewCard = crew) {
                        navigationToCrewDetailScreen(crew.id)
                    }
                }
            }
        }

    }
}