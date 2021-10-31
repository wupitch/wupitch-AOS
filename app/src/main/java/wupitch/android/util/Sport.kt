package wupitch.android.util

import wupitch.android.R

enum class Sport(val no : Int, val sportName : Int, val color: Int, val icon : Int) {
    BASKETBALL(0, R.string.basketball, R.color.blue_basketball, R.drawable.image_69),
    SOCCER(1, R.string.soccer_football, R.color.red_soccer, R.drawable.image_70),
    BADMINTON(2, R.string.badminton, R.color.green_badminton, R.drawable.image_72),
    VOLLEYBALL(3, R.string.volleyball, R.color.pink_volleyball, R.drawable.image_71),
    RUNNING(4, R.string.running, R.color.yellow_running, R.drawable.image_74),
    HIKING(5, R.string.hiking, R.color.purple_hiking, R.drawable.image_73)
}