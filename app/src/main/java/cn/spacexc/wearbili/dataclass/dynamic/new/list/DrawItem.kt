package cn.spacexc.wearbili.dataclass.dynamic.new.list


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrawItem(
    @SerializedName("height")
    val height: Int,
    @SerializedName("size")
    val size: Double,
    @SerializedName("src")
    val src: String,
    @SerializedName("width")
    val width: Int
) : Parcelable