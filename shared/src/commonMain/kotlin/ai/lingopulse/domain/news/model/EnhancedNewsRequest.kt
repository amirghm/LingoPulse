package ai.lingopulse.domain.news.model

data class EnhancedNewsRequest(
    val article: String,
    val targetLanguage: String,
    val languageLevel: String
)
