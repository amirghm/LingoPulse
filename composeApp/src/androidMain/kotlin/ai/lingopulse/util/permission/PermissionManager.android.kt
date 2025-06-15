package ai.lingopulse.util.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

actual class PermissionManager(
    private val context: Context
) {
    
    actual fun checkAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    actual fun requestAudioPermission(onResult: (Boolean) -> Unit) {
        if (checkAudioPermission()) {
            onResult(true)
            return
        }
        
        // For now, just return false if permission is not granted
        // In a real implementation, you'd need to handle this at a higher level
        // or use a different permission request mechanism
        onResult(false)
    }
}
