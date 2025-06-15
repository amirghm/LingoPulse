package ai.lingopulse.util.audio

actual class AudioRecorder {
    actual fun startRecording(): String? {
        // TODO: Implement iOS audio recording
        println("iOS Audio recording not implemented yet")
        return null
    }
    
    actual fun stopRecording(): String? {
        // TODO: Implement iOS audio recording
        println("iOS Audio recording not implemented yet")
        return null
    }
    
    actual fun isRecording(): Boolean {
        return false
    }
    
    actual fun release() {
        // TODO: Implement iOS audio recording cleanup
    }
}