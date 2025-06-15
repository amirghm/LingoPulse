package ai.lingopulse.ui.feature.details

import ai.lingopulse.presentation.details.model.UiFeedDetails
import ai.lingopulse.ui.core.common.model.ScreenEvent

sealed class FeedDetailsScreenEvent: ScreenEvent() {
    data object OnBackClicked : FeedDetailsScreenEvent()
    data class OnStartConversationClicked(val feedDetails: UiFeedDetails) : FeedDetailsScreenEvent()
}