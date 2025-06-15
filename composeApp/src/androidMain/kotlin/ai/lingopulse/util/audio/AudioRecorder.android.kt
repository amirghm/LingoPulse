package ai.lingopulse.util.audio

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import java.io.File

actual class AudioRecorder(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var currentOutputFile: File? = null
    
    actual fun startRecording(): String? {
        println("🎙️ [AudioRecorder] startRecording() called")
        return try {
            val outputFile = File(context.cacheDir, "voice_recording_${System.currentTimeMillis()}.m4a")
            currentOutputFile = outputFile
            println("📁 [AudioRecorder] Created output file: ${outputFile.absolutePath}")
            
            mediaRecorder = MediaRecorder().apply {
                println("🔧 [AudioRecorder] Setting up MediaRecorder...")
                setAudioSource(MediaRecorder.AudioSource.MIC)
                println("✅ [AudioRecorder] Audio source set to MIC")
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                println("✅ [AudioRecorder] Output format set to MPEG_4")
                setOutputFile(outputFile.absolutePath)
                println("✅ [AudioRecorder] Output file set: ${outputFile.absolutePath}")
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                println("✅ [AudioRecorder] Audio encoder set to AAC")
                
                println("🔄 [AudioRecorder] Preparing MediaRecorder...")
                prepare()
                println("✅ [AudioRecorder] MediaRecorder prepared successfully")
                
                println("▶️ [AudioRecorder] Starting recording...")
                start()
                println("✅ [AudioRecorder] Recording started successfully")
            }
            
            Log.d("AudioRecorder", "Recording started: ${outputFile.absolutePath}")
            println("🎤 [AudioRecorder] Recording started successfully: ${outputFile.absolutePath}")
            outputFile.absolutePath
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error starting recording", e)
            println("❌ [AudioRecorder] Error starting recording: ${e.message}")
            println("📊 [AudioRecorder] Exception stack trace: ${e.stackTraceToString()}")
            null
        }
    }
    
    actual fun stopRecording(): String? {
        println("🛑 [AudioRecorder] stopRecording() called")
        return try {
            mediaRecorder?.apply {
                println("🔄 [AudioRecorder] Stopping MediaRecorder...")
                stop()
                println("✅ [AudioRecorder] MediaRecorder stopped")
                
                println("🧹 [AudioRecorder] Releasing MediaRecorder...")
                release()
                println("✅ [AudioRecorder] MediaRecorder released")
            }
            val filePath = currentOutputFile?.absolutePath
            Log.d("AudioRecorder", "Recording stopped: $filePath")
            println("📁 [AudioRecorder] Recording stopped: $filePath")
            
            // Check if file exists and has content
            currentOutputFile?.let { file ->
                if (file.exists()) {
                    println("✅ [AudioRecorder] File exists, size: ${file.length()} bytes")
                } else {
                    println("❌ [AudioRecorder] File does not exist!")
                }
            }
            
            filePath
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error stopping recording", e)
            println("❌ [AudioRecorder] Error stopping recording: ${e.message}")
            println("📊 [AudioRecorder] Exception stack trace: ${e.stackTraceToString()}")
            null
        } finally {
            mediaRecorder = null
            currentOutputFile = null
            println("🧹 [AudioRecorder] Cleanup completed")
        }
    }
    
    actual fun isRecording(): Boolean {
        val recording = mediaRecorder != null
        println("❓ [AudioRecorder] isRecording(): $recording")
        return recording
    }
    
    actual fun release() {
        println("🧹 [AudioRecorder] release() called")
        try {
            mediaRecorder?.apply {
                if (isRecording()) {
                    println("🛑 [AudioRecorder] Stopping active recording before release...")
                    stop()
                }
                println("🧹 [AudioRecorder] Releasing MediaRecorder...")
                release()
            }
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error releasing recorder", e)
            println("❌ [AudioRecorder] Error releasing recorder: ${e.message}")
        } finally {
            mediaRecorder = null
            currentOutputFile = null
            println("✅ [AudioRecorder] Release completed")
        }
    }
}
