package cn.spacexc.wearbili.dataclass.video.rcmd.web


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("business_card")
    val businessCard: Any?,
    @SerializedName("floor_info")
    val floorInfo: List<FloorInfo>,
    @SerializedName("item")
    val item: List<Item>,
    @SerializedName("mid")
    val mid: Long,
    @SerializedName("preload_expose_pct")
    val preloadExposePct: Double,
    @SerializedName("preload_floor_expose_pct")
    val preloadFloorExposePct: Double,
    @SerializedName("user_feature")
    val userFeature: Any?
)