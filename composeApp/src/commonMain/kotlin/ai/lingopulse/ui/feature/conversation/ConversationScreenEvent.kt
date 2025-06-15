package ai.lingopulse.ui.feature.conversation

import ai.lingopulse.presentation.details.model.UiFeedDetails
import ai.lingopulse.ui.core.common.model.ScreenEvent

sealed class ConversationScreenEvent: ScreenEvent() {
    object OnBackClicked : ConversationScreenEvent()
    object OnStartListening : ConversationScreenEvent()
    object OnStopListening : ConversationScreenEvent()
    object OnStopTalking : ConversationScreenEvent()
    object OnRetryAfterError : ConversationScreenEvent()
    data class OnVoiceInputReceived(val audioData: ByteArray) : ConversationScreenEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as OnVoiceInputReceived

            if (!audioData.contentEquals(other.audioData)) return false

            return true
        }

        override fun hashCode(): Int {
            return audioData.contentHashCode()
        }
    }
    data class OnAudioPlaybackFinished(val messageId: String) : ConversationScreenEvent()
}
