package ai.lingopulse.presentation.details.mapper

import ai.lingopulse.domain.common.model.FeedDetails
import ai.lingopulse.presentation.details.model.UiFeedDetails

fun FeedDetails.toUiFeedDetails(): UiFeedDetails {
    return UiFeedDetails(
        id = this.id,
        category = this.category,
        title = this.title,
        subtitle = this.subtitle,
        author = this.author,
        url = this.url,
        textContent = this.content,
        imageUrl = this.imageUrl,
        publishedAt = this.publishedAt
    )
}
