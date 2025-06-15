package ai.lingopulse.presentation.details.model

data class UiFeedDetails(
    val id: String,
    val category: String,
    val title: String,
    val subtitle: String,
    val textContent: String,
    val author: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: String
)