package cn.spacexc.wearbili.dataclass.dynamic.new.detail

import cn.spacexc.wearbili.dataclass.dynamic.new.list.Data
import cn.spacexc.wearbili.dataclass.dynamic.new.list.DynamicItem
import com.google.gson.annotations.SerializedName

/**
 * Created by XC-Qan on 2023/1/15.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

data class DynamicDetail(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("ttl")
    val ttl: Int
) {
    data class Data(val item: DynamicItem)
}