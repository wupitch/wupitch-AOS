package wupitch.android.util

import wupitch.android.R

enum class Sport(val no : Int, val sportName : Int, val color : Int,  val detailImage : Int, val thumbnailImage : Int) {
    SOCCER(0, R.string.soccer_football, R.color.red_soccer,R.drawable.img_foot_thumb,R.drawable.img_foot ),
    BADMINTON(1, R.string.badminton,R.color.green_badminton, R.drawable.img_bad_thumb, R.drawable.img_bad),
    VOLLEYBALL(2, R.string.volleyball,  R.color.pink_volleyball,R.drawable.img_voll_thumb, R.drawable.img_voll),
    BASKETBALL(3, R.string.basketball, R.color.blue_basketball, R.drawable.img_bask_thumb, R.drawable.img_basket),
    HIKING(5, R.string.hiking,  R.color.purple_hiking,R.drawable.img_hike_thumb, R.drawable.img_hike),
    RUNNING(4, R.string.running, R.color.yellow_running,R.drawable.img_run_thumb, R.drawable.img_run);

    companion object {
        fun getNumOf(no : Int) = values().first {it.no == no}
    }
}