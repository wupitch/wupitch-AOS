package wupitch.android.presentation

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object ImpromptuScreen: Screen("impromptu_screen")
    object MyActivityScreen: Screen("my_activity_screen")
    object FeedScreen: Screen("feed_screen")
    object ProfileScreen: Screen("profile_screen")
}
