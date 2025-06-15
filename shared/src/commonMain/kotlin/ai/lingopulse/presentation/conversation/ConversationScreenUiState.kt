package ai.lingopulse.presentation.conversation

import ai.lingopulse.presentation.details.model.UiFeedDetails

import ai.lingopulse.presentation.common.model.ConversationState

sealed class ConversationScreenUiState {
    data class Loading(
        val feedDetails: UiFeedDetails
    ) : ConversationScreenUiState()
    
    data class ConversationReady(
        val feedDetails: UiFeedDetails,
        val conversationState: ConversationState,
        val conversationId: String? = null
    ) : ConversationScreenUiState()
    
    data class Error(
        val feedDetails: UiFeedDetails,
        val message: String? = null
    ) : ConversationScreenUiState()
}
