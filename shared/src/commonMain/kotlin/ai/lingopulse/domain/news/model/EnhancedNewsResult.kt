package ai.lingopulse.domain.news.model

data class EnhancedNewsResult(
    val success: Boolean,
    val enhancedContent: String? = null,
    val error: String? = null
)
