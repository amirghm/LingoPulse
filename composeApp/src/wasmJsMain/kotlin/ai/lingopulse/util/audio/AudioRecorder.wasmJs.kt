package ai.lingopulse.util.audio

actual class AudioRecorder {
    actual fun startRecording(): String? {
        // TODO: Implement WASM JS audio recording
        println("WASM JS Audio recording not implemented yet")
        return null
    }
    
    actual fun stopRecording(): String? {
        // TODO: Implement WASM JS audio recording
        println("WASM JS Audio recording not implemented yet")
        return null
    }
    
    actual fun isRecording(): Boolean {
        return false
    }
    
    actual fun release() {
        // TODO: Implement WASM JS audio recording cleanup
    }
}