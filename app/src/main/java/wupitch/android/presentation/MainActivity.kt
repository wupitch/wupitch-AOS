package wupitch.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import wupitch.android.R
import wupitch.android.presentation.feed.FeedScreen
import wupitch.android.presentation.home.HomeScreen
import wupitch.android.presentation.impromptu.ImpromptuScreen
import wupitch.android.presentation.my_activity.MyActivityScreen
import wupitch.android.presentation.profile.ProfileScreen
import wupitch.android.presentation.theme.Gray03
import wupitch.android.presentation.theme.WupitchTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WupitchTheme {
                Surface(color = Color.White) {
                    val navController = rememberNavController()

                    Scaffold( //1. scaffold 의 bottomBar 에 bottomNavigationBar 을 지정한다.
                        bottomBar = {
                            BottomNavigationBar(
                                //각각의 item, 각 item click 핸들하는 navcontroller, onclick 을 인자로 만든다.
                                items = listOf(
                                    BottomNavItem(
                                        name = R.string.home,
                                        route = "home",
                                        icon = R.drawable.ic_home
                                    ),
                                    BottomNavItem(
                                        name = R.string.impromptu,
                                        route = "impromptu",
                                        icon = R.drawable.ic_bungae,
                                    ),
                                    BottomNavItem(
                                        name = R.string.my_activity,
                                        route = "my_activity",
                                        icon = R.drawable.ic_myact,
                                    ),
                                    BottomNavItem(
                                        name = R.string.feed,
                                        route = "feed",
                                        icon = R.drawable.ic_feed,
                                    ),
                                    BottomNavItem(
                                        name = R.string.profile,
                                        route = "profile",
                                        icon = R.drawable.ic_profile,
                                    )
                                ),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route)
                                    //어떤 원리로 이동하는 거임??
                                }
                            )
                        }
                    ) {
                        Navigation(navController = navController)
                        //padding values 로 왜 이걸 넣지??
                    }
                }
            }
        }
    }
}
//??
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen()
        }
        composable("impromptu") {
            ImpromptuScreen()
        }
        composable("my_activity") {
            MyActivityScreen()
        }
        composable("feed") {
            FeedScreen()
        }
        composable("profile") {
            ProfileScreen()
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState() //??
    BottomNavigation( // default composable.
        modifier = modifier,
        backgroundColor = Color.White,
        elevation = 5.dp,
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem( //default composable.
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Gray03,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                modifier = modifier.padding(top = 8.dp),
                                painter = painterResource(id = item.icon),
                                contentDescription = stringResource(id = item.name)
                            )
                            Text(
                                text = stringResource(id = item.name),
                                textAlign = TextAlign.Center,
                                fontSize = 11.sp,
                                modifier = modifier.padding(
                                    bottom = 7.dp
                                )
                            )
                    }
                }
            )
        }
    }
}