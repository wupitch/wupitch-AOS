package wupitch.android.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto

@Composable
fun RepetitionLayout(
    text: Int? = null,
    filterItemList: List<FilterItem>,
    checkedListState : SnapshotStateList<Int>,
    modifier: Modifier,
) {

    Column(Modifier.fillMaxWidth()) {
        if(text != null) {
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(id = text),
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        FlowRow(
            modifier = Modifier
                .padding(top = 12.dp),
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 16.dp
        ) {
            filterItemList.forEachIndexed { index, item ->
                ToggleBtn(
                    toggleState = item.state,
                    modifier = modifier,
                    textString = item.name,
                ) {
                    if (it) {
                        if(!checkedListState.contains(index)) {
                            checkedListState.add(index)
                        }
                    }else {
                        if(checkedListState.contains(index)) {
                            checkedListState.remove(index)
                        }
                    }
                }
            }
        }
    }
}