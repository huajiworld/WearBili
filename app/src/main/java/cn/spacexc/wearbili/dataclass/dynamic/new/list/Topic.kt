package cn.spacexc.wearbili.dataclass.dynamic.new.list


import com.google.gson.annotations.SerializedName

data class Topic(
    @SerializedName("id")
    val id: Long,
    @SerializedName("jump_url")
    val jumpUrl: String,
    @SerializedName("name")
    val name: String
)