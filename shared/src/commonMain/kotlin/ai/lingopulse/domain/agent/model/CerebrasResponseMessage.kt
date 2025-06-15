package ai.lingopulse.domain.agent.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CerebrasResponseMessage(
    @SerialName("agent") val agent: String,
    @SerialName("message") val message: String,
    @SerialName("app_name") val appName: String? = null,
    @SerialName("app_color") val appColor: String? = null,
    @SerialName("app_emoji_icon") val appEmojiIcon: String? = null,
    @SerialName("app_description") val appDescription: String? = null,
    @SerialName("content") val content: String? = null
)