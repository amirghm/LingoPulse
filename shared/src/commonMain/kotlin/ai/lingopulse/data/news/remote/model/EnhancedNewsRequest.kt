package ai.lingopulse.data.news.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class EnhancedNewsRequest(
    val article: String,
    val targetLanguage: String,
    val languageLevel: String
)
