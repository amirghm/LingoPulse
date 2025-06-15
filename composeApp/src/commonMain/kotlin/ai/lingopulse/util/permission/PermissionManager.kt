package ai.lingopulse.util.permission

expect class PermissionManager {
    fun checkAudioPermission(): Boolean
    fun requestAudioPermission(onResult: (Boolean) -> Unit)
}