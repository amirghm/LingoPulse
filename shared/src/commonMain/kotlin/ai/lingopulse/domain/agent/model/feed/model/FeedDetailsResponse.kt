package ai.lingopulse.domain.agent.model.feed.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedDetailsResponse(
    @SerialName("id") val id: String,
    @SerialName("category") val category: String,
    @SerialName("title") val title: String,
    @SerialName("subtitle") val subtitle: String,
    @SerialName("author") val author: String,
    @SerialName("url") val url: String,
    @SerialName("urlToImage") val imageUrl: String,
    @SerialName("publishedAt") val publishedAt: String,
    @SerialName("content") val content: String
)