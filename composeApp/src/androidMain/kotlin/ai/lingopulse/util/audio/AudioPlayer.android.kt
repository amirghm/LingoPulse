package ai.lingopulse.util.audio

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

actual class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentOnComplete: (() -> Unit)? = null

    actual fun playAudio(audioData: ByteArray, onComplete: () -> Unit) {
        try {
            println("🔊 [AudioPlayer] Playing audio from ByteArray, size: ${audioData.size} bytes")
            
            // Validate audio data
            if (audioData.isEmpty()) {
                println("❌ [AudioPlayer] Audio data is empty")
                onComplete()
                return
            }
            
            if (audioData.size < 100) {
                println("⚠️ [AudioPlayer] Audio data seems too small (${audioData.size} bytes), might be invalid")
            }
            
            // Check if it looks like MP3 data (starts with ID3 tag or MP3 frame sync)
            val isLikelyMp3 = audioData.size >= 3 && (
                (audioData[0] == 'I'.code.toByte() && audioData[1] == 'D'.code.toByte() && audioData[2] == '3'.code.toByte()) || // ID3 tag
                (audioData[0] == 0xFF.toByte() && (audioData[1].toInt() and 0xE0) == 0xE0) // MP3 frame sync
            )
            
            if (!isLikelyMp3) {
                println("⚠️ [AudioPlayer] Audio data doesn't look like valid MP3 format")
                println("🔍 [AudioPlayer] First 10 bytes: ${audioData.take(10).joinToString(" ") { "%02x".format(it) }}")
            }
            
            // Save audio data to a temporary file and play it
            val tempFile = File(context.cacheDir, "temp_audio_${System.currentTimeMillis()}.mp3")
            FileOutputStream(tempFile).use { it.write(audioData) }
            
            println("📁 [AudioPlayer] Saved temp file: ${tempFile.absolutePath}, size: ${tempFile.length()} bytes")
            
            playAudioFromFile(tempFile.absolutePath) {
                println("🗑️ [AudioPlayer] Cleaning up temp file: ${tempFile.absolutePath}")
                if (tempFile.exists()) {
                    tempFile.delete()
                }
                onComplete()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error playing audio from data", e)
            println("❌ [AudioPlayer] Error playing audio from ByteArray: ${e.message}")
            onComplete() // Still call onComplete to prevent UI state issues
        }
    }

    actual fun playAudioFromFile(filePath: String, onComplete: () -> Unit) {
        try {
            println("🎵 [AudioPlayer] Starting playback from file: $filePath")
            
            // Validate file exists and has content
            val file = File(filePath)
            if (!file.exists()) {
                println("❌ [AudioPlayer] File does not exist: $filePath")
                onComplete()
                return
            }
            
            if (file.length() == 0L) {
                println("❌ [AudioPlayer] File is empty: $filePath")
                onComplete()
                return
            }
            
            println("📊 [AudioPlayer] File info: exists=${file.exists()}, size=${file.length()} bytes")
            
            // Clean up any existing MediaPlayer
            cleanupMediaPlayer()
            
            currentOnComplete = onComplete
            mediaPlayer = MediaPlayer().apply {
                println("🔧 [AudioPlayer] Setting up MediaPlayer...")
                
                setOnPreparedListener { mp ->
                    println("✅ [AudioPlayer] MediaPlayer prepared, starting playback")
                    try {
                        mp.start()
                        println("▶️ [AudioPlayer] Playback started successfully")
                    } catch (e: Exception) {
                        Log.e("AudioPlayer", "Error starting playback", e)
                        println("❌ [AudioPlayer] Error starting playback: ${e.message}")
                        handlePlaybackComplete()
                    }
                }
                
                setOnCompletionListener {
                    println("✅ [AudioPlayer] Playback completed successfully")
                    handlePlaybackComplete()
                }
                
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayer", "MediaPlayer error: what=$what, extra=$extra")
                    println("❌ [AudioPlayer] MediaPlayer error: what=$what, extra=$extra")
                    
                    // Log detailed error information
                    val errorMessage = when (what) {
                        MediaPlayer.MEDIA_ERROR_UNKNOWN -> "MEDIA_ERROR_UNKNOWN"
                        MediaPlayer.MEDIA_ERROR_SERVER_DIED -> "MEDIA_ERROR_SERVER_DIED"
                        else -> "UNKNOWN_ERROR_CODE($what)"
                    }
                    
                    val extraMessage = when (extra) {
                        MediaPlayer.MEDIA_ERROR_IO -> "MEDIA_ERROR_IO"
                        MediaPlayer.MEDIA_ERROR_MALFORMED -> "MEDIA_ERROR_MALFORMED"
                        MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> "MEDIA_ERROR_UNSUPPORTED"
                        MediaPlayer.MEDIA_ERROR_TIMED_OUT -> "MEDIA_ERROR_TIMED_OUT"
                        -2147483648 -> "MEDIA_ERROR_SYSTEM"
                        else -> "UNKNOWN_EXTRA_CODE($extra)"
                    }
                    
                    println("🔍 [AudioPlayer] Error details: $errorMessage, extra: $extraMessage")
                    println("📁 [AudioPlayer] Failed file: $filePath")
                    
                    handlePlaybackComplete()
                    true // Return true to indicate we handled the error
                }
                
                try {
                    setDataSource(filePath)
                    prepareAsync()
                } catch (e: Exception) {
                    Log.e("AudioPlayer", "Error setting data source", e)
                    println("❌ [AudioPlayer] Error setting data source: ${e.message}")
                    handlePlaybackComplete()
                }
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error playing audio from file: $filePath", e)
            println("❌ [AudioPlayer] Error playing audio from file: ${e.message}")
            onComplete() // Still call onComplete to prevent UI state issues
        }
    }

    actual fun stopPlayback() {
        println("🛑 [AudioPlayer] Stopping playback")
        cleanupMediaPlayer()
        currentOnComplete = null
    }

    actual fun isPlaying(): Boolean {
        return try {
            mediaPlayer?.isPlaying ?: false
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error checking if playing", e)
            println("❌ [AudioPlayer] Error checking if playing: ${e.message}")
            false
        }
    }

    actual fun release() {
        println("🧹 [AudioPlayer] Releasing AudioPlayer")
        cleanupMediaPlayer()
    }
    
    private fun cleanupMediaPlayer() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    println("🛑 [AudioPlayer] Stopping active playback")
                    stop()
                }
                println("🧹 [AudioPlayer] Releasing MediaPlayer")
                release()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error during MediaPlayer cleanup", e)
            println("❌ [AudioPlayer] Error during cleanup: ${e.message}")
        } finally {
            mediaPlayer = null
        }
    }
    
    private fun handlePlaybackComplete() {
        println("🔄 [AudioPlayer] Handling playback completion")
        val callback = currentOnComplete
        cleanupMediaPlayer()
        currentOnComplete = null
        callback?.invoke()
    }
}
