package wupitch.android.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import wupitch.android.R

@Composable
fun CreateFab(
    modifier: Modifier,
    onClick : () -> Unit
) {
    FloatingActionButton(
        modifier = modifier.size(56.dp),
        shape = CircleShape,
        backgroundColor = colorResource(id = R.color.main_black),
        elevation = FloatingActionButtonDefaults.elevation(10.dp),
        onClick = {
            onClick()
        }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_btn_06_add),
            contentDescription = "fab icon",
            tint = Color.White
        )
    }
}