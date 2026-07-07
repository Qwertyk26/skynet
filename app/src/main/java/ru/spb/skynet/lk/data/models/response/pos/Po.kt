package ru.spb.skynet.lk.data.models.response.pos


import com.google.gson.annotations.SerializedName

data class Po(
    @SerializedName("address")
    var address: String?,
    @SerializedName("bl_home_ID")
    var blHomeID: Int?,
    @SerializedName("bl_map_coordinates")
    var blMapCoordinates: Any?,
    @SerializedName("bl_mufta_id")
    var blMuftaId: Any?,
    @SerializedName("ID")
    var iD: Int?,
    @SerializedName("location_flat")
    var locationFlat: String?,
    @SerializedName("location_floor")
    var locationFloor: String?,
    @SerializedName("services_types")
    var servicesTypes: List<Int?>?
)