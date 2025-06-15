package ai.lingopulse.util

import com.russhwolf.settings.Settings
import kotlinx.browser.localStorage
import org.w3c.dom.Storage
import org.w3c.dom.get
import org.w3c.dom.set

class StorageSettings(
    private val name: String,
    private val delegate: Storage = localStorage
) : Settings {

    private fun prefixed(key: String): String = "$name:$key"

    override val keys: Set<String>
        get() = List(size) { delegate.key(it)!! }
            .filter { it.startsWith("$name:") }
            .map { it.removePrefix("$name:") }
            .toSet()

    override val size: Int
        get() = delegate.length

    override fun clear() {
        val keysToRemove = keys
        keysToRemove.forEach { remove(it) }
    }

    override fun remove(key: String) {
        delegate.removeItem(prefixed(key))
    }

    override fun hasKey(key: String): Boolean {
        return delegate[prefixed(key)] != null
    }

    override fun putInt(key: String, value: Int) {
        delegate[prefixed(key)] = value.toString()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return delegate[prefixed(key)]?.toInt() ?: defaultValue
    }

    override fun getIntOrNull(key: String): Int? {
        return delegate[prefixed(key)]?.toIntOrNull()
    }

    override fun putLong(key: String, value: Long) {
        delegate[prefixed(key)] = value.toString()
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return delegate[prefixed(key)]?.toLong() ?: defaultValue
    }

    override fun getLongOrNull(key: String): Long? {
        return delegate[prefixed(key)]?.toLongOrNull()
    }

    override fun putString(key: String, value: String) {
        delegate[prefixed(key)] = value
    }

    override fun getString(key: String, defaultValue: String): String {
        return delegate[prefixed(key)] ?: defaultValue
    }

    override fun getStringOrNull(key: String): String? {
        return delegate[prefixed(key)]
    }

    override fun putFloat(key: String, value: Float) {
        delegate[prefixed(key)] = value.toString()
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return delegate[prefixed(key)]?.toFloat() ?: defaultValue
    }

    override fun getFloatOrNull(key: String): Float? {
        return delegate[prefixed(key)]?.toFloatOrNull()
    }

    override fun putDouble(key: String, value: Double) {
        delegate[prefixed(key)] = value.toString()
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return delegate[prefixed(key)]?.toDouble() ?: defaultValue
    }

    override fun getDoubleOrNull(key: String): Double? {
        return delegate[prefixed(key)]?.toDoubleOrNull()
    }

    override fun putBoolean(key: String, value: Boolean) {
        delegate[prefixed(key)] = value.toString()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return delegate[prefixed(key)]?.toBoolean() ?: defaultValue
    }

    override fun getBooleanOrNull(key: String): Boolean? {
        return delegate[prefixed(key)]?.toBoolean()
    }
}