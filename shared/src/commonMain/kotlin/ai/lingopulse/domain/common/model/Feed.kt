package ai.lingopulse.domain.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    val id: String,
    val category: String,
    val title: String,
    val subtitle: String,
    val content: String,
    val author: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: String
)