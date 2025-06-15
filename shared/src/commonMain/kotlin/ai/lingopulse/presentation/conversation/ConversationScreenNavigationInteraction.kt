package ai.lingopulse.presentation.conversation

sealed class ConversationScreenNavigationInteraction {
    data object NavigateUp : ConversationScreenNavigationInteraction()
}