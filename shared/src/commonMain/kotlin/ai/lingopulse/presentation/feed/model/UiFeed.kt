package ai.lingopulse.presentation.feed.model

import ai.lingopulse.presentation.common.model.UiCategory
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class UiFeed(
    val id: String,
    val category: UiCategory,
    val title: String,
    val subtitle: String,
    val content: String,
    val author: String,
    val imageUrl: String,
    val url: String,
    val publishedAt: String
) {
    @OptIn(ExperimentalTime::class)
    val timestamp: Long
        get() = try {
            Instant.parse(publishedAt).toEpochMilliseconds()
        } catch (_: Exception) {
            0L
        }

}