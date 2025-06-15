package ai.lingopulse.util.permission

actual class PermissionManager {
    actual fun checkAudioPermission(): Boolean {
        // TODO: Implement WASM JS permission checking
        return true // Dummy implementation
    }
    
    actual fun requestAudioPermission(onResult: (Boolean) -> Unit) {
        // TODO: Implement WASM JS permission request
        onResult(true) // Dummy implementation
    }
}