package ai.lingopulse.util.audio

expect class AudioPlayer {
    fun playAudio(audioData: ByteArray, onComplete: () -> Unit)
    fun playAudioFromFile(filePath: String, onComplete: () -> Unit)
    fun stopPlayback()
    fun isPlaying(): Boolean
    fun release()
}