package wupitch.android.presentation.ui.main.my_activity

import android.os.Parcel
import android.os.Parcelable

data class MyCrewArg(
    val crewId : Int = -1,
    val selectedTab : Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(crewId)
        parcel.writeInt(selectedTab)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyCrewArg> {
        override fun createFromParcel(parcel: Parcel): MyCrewArg {
            return MyCrewArg(parcel)
        }

        override fun newArray(size: Int): Array<MyCrewArg?> {
            return arrayOfNulls(size)
        }
    }

}
