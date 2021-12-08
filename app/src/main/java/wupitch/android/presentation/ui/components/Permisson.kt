package wupitch.android.presentation.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@ExperimentalPermissionsApi
@Composable
fun Permission(
    permission: List<String>,
    permissionNotAvailableContent: @Composable () -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    permission.forEach {
        val permissionState = rememberPermissionState(it)
        PermissionRequired(
            permissionState = permissionState,
            permissionNotGrantedContent = { //허용이 안 되어 있어서 (처음이든 이후든) 요청할 때.
                SideEffect {
                    permissionState.launchPermissionRequest()
                }
            },
           permissionNotAvailableContent = { //처음에 거부 버튼 눌렀을 때. & 거부된 상태일 때.
               permissionNotAvailableContent()
//                SideEffect {
//                    permissionState.launchPermissionRequest() //거부할 때든, 되어있든 하면 다시 리퀘스트 하지 않는다. 세팅으로 가게 해야 하는 듯.
//                }
            } ,
            content = content
        )
    }

}