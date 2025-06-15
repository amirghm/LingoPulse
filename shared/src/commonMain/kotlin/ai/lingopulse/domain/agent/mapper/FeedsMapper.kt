package ai.lingopulse.domain.agent.mapper

import ai.lingopulse.domain.common.model.Feed
import ai.lingopulse.domain.common.model.FeedDetails
import ai.lingopulse.domain.agent.model.feed.model.FeedDetailsResponse
import ai.lingopulse.domain.agent.model.feed.model.FeedResponse

fun List<FeedResponse>.toFeed() = map {
    Feed(
        id = it.id,
        category = it.category,
        title = it.title,
        subtitle = it.subtitle,
        content = it.content,
        url = it.url,
        author = it.author,
        imageUrl = it.imageUrl,
        publishedAt = it.publishedAt
    )
}

fun FeedDetailsResponse.toFeedDetails() = FeedDetails(
    id = id,
    category = category,
    title = title,
    subtitle = subtitle,
    url = url,
    author = author,
    imageUrl = imageUrl,
    publishedAt = publishedAt,
    content = content
)