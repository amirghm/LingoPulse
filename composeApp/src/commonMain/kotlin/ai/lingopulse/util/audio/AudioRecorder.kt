package ai.lingopulse.util.audio

expect class AudioRecorder {
    fun startRecording(): String?
    fun stopRecording(): String?
    fun isRecording(): Boolean
    fun release()
}