package ai.lingopulse.presentation.feed.mapper

import ai.lingopulse.domain.common.model.Category
import ai.lingopulse.domain.common.model.Feed
import ai.lingopulse.presentation.common.mapper.toUiCategory
import ai.lingopulse.presentation.common.model.UiCategory
import ai.lingopulse.presentation.feed.model.UiFeed

fun Feed.toUiFeed(category: UiCategory) = UiFeed(
    id = this.id,
    category = category,
    title = this.title,
    subtitle = this.subtitle,
    content = this.content,
    url = this.url,
    author = this.author,
    imageUrl = this.imageUrl,
    publishedAt = this.publishedAt
)

fun List<Feed>.toUiFeeds(categories: List<Category>) = this.mapNotNull { feed ->
    val category = categories.firstOrNull { category -> category.id == feed.category } ?: return@mapNotNull null
    feed.toUiFeed(category.toUiCategory(true))
}
