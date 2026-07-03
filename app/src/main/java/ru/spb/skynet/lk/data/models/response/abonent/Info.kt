package ru.spb.skynet.lk.data.models.response.abonent


import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("bl_s1_policy_lk_type")
    var blS1PolicyLkType: String?,
    @SerializedName("bl_ur_title")
    var blUrTitle: Any?,
    @SerializedName("bl_user_pos")
    var blUserPos: Int?,
    @SerializedName("bober_data")
    var boberData: Boolean?,
    @SerializedName("can_pay")
    var canPay: Boolean?,
    @SerializedName("hasActiveAvans")
    var hasActiveAvans: Boolean?,
    @SerializedName("has_chat")
    var hasChat: Boolean?,
    @SerializedName("has_pincode")
    var hasPincode: Boolean?,
    @SerializedName("has_telegram")
    var hasTelegram: Boolean?,
    @SerializedName("ipv6_support")
    var ipv6Support: Boolean?,
    @SerializedName("is_ur")
    var isUr: Boolean?,
    @SerializedName("managers_contacts")
    var managersContacts: Any?,
    @SerializedName("menu")
    var menu: List<Menu?>?,
    @SerializedName("money")
    var money: String?,
    @SerializedName("pay_card_info")
    var payCardInfo: PayCardInfo?,
    @SerializedName("public_wifi_access")
    var publicWifiAccess: Boolean?,
    @SerializedName("sensitive_actions_access")
    var sensitiveActionsAccess: List<SensitiveActionsAcces?>?,
    @SerializedName("unread_count")
    var unreadCount: Int?,
    @SerializedName("user_id")
    var userId: Int?
)