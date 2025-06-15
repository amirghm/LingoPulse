package ai.lingopulse.presentation.details

import ai.lingopulse.domain.common.model.FeedDetails

sealed class FeedDetailsScreenNavigationInteraction {
    data object NavigateUp : FeedDetailsScreenNavigationInteraction()
    data class OpenConversation(val feedDetails: FeedDetails) : FeedDetailsScreenNavigationInteraction()
}