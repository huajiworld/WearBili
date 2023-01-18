package cn.spacexc.wearbili.dataclass.dynamic.new.list


import com.google.gson.annotations.SerializedName

data class ItemRichTextNode(
    @SerializedName("emoji")
    val emoji: Emoji?,
    @SerializedName("jump_url")
    val jumpUrl: String?,
    @SerializedName("orig_text")
    val origText: String,
    @SerializedName("rid")
    val rid: String?,
    @SerializedName("text")
    val text: String,
    @SerializedName("type")
    val type: String
)