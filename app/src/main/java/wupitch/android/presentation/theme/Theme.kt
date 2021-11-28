package wupitch.android.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import wupitch.android.R

private val DarkColorPalette = darkColors(
    primary = ColorPrimary,
    background = DarkGray,
    onBackground = TextWhite,
    onPrimary = DarkGray
)

private val LightColorPalette = lightColors(
    primary = ColorPrimary,
    background = Color.White,
    onBackground = MediumGray,
    onPrimary = DarkGray
)



@Composable
fun WupitchTheme(darkTheme: Boolean = false, content: @Composable() () -> Unit) {

    val systemUiController = rememberSystemUiController()
    systemUiController.apply {
        setStatusBarColor(colorResource(id = R.color.white))
        setNavigationBarColor(colorResource(id = R.color.bottom_nav_color))
    }

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}


@Composable
fun CameraTheme(darkTheme: Boolean = false, content: @Composable() () -> Unit) {

    val systemUiController = rememberSystemUiController()
    systemUiController.apply {
        setStatusBarColor(colorResource(id = R.color.main_black))
        setNavigationBarColor(colorResource(id = R.color.main_black))
    }

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun SplashTheme(darkTheme: Boolean = false, content: @Composable() () -> Unit) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        colorResource(id = R.color.main_orange)
    )

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}