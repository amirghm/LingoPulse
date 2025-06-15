package ai.lingopulse.util.permission

actual class PermissionManager {
    actual fun checkAudioPermission(): Boolean {
        // TODO: Implement iOS permission checking
        return true // Dummy implementation
    }
    
    actual fun requestAudioPermission(onResult: (Boolean) -> Unit) {
        // TODO: Implement iOS permission request
        onResult(true) // Dummy implementation
    }
}