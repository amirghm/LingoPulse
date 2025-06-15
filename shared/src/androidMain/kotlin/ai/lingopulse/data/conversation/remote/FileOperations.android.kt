package ai.lingopulse.data.conversation.remote

import java.io.File

actual fun readFileBytes(filePath: String): ByteArray {
    val file = File(filePath)
    println("📂 [FileOperations] Reading file: $filePath")
    println("📏 [FileOperations] File exists: ${file.exists()}, size: ${file.length()} bytes")
    
    if (!file.exists()) {
        println("❌ [FileOperations] File does not exist: $filePath")
        throw IllegalArgumentException("File does not exist: $filePath")
    }
    
    if (file.length() == 0L) {
        println("⚠️ [FileOperations] Warning: File is empty: $filePath")
    }
    
    val bytes = file.readBytes()
    println("✅ [FileOperations] Successfully read ${bytes.size} bytes from file")
    return bytes
}

actual fun getFileName(filePath: String): String {
    val fileName = File(filePath).name
    println("📝 [FileOperations] Extracted filename: $fileName from path: $filePath")
    return fileName
}
