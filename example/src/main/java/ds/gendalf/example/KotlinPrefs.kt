package ds.gendalf.example

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty


/**
 * Instead of Java Annotation Processor you can use Kotlin Delegates :)
 */
object KotlinPrefsSetup {
    lateinit var prefs: SharedPreferences
    fun init(ctx: Context, name: String) {
        prefs = ctx.getSharedPreferences(name, Context.MODE_PRIVATE)
    }
}

fun prefsKey<T>(default: T): PrefsDelegate<T> = PrefsDelegate(default)

private open class PrefsDelegate<T>(val default: T) : ReadWriteProperty<Any?, T> {

    protected val prefs: SharedPreferences = KotlinPrefsSetup.prefs
    var value: T = default

    @Suppress("unchecked_cast")
    override fun get(thisRef: Any?, property: PropertyMetadata): T {
        val n = property.name
        when (value) {
            is String -> return prefs.getString(n, default as String) as T
            is Int -> return prefs.getInt(n, default as Int) as T
            is Long -> return prefs.getLong(n, default as Long) as T
            is Float -> return prefs.getFloat(n, default as Float) as T
            is Boolean -> return prefs.getBoolean(n, default as Boolean) as T
            is Set<*> -> return prefs.getStringSet(n, default as Set<String>) as T
            else -> throw IllegalArgumentException()
        }
    }

    @Suppress("unchecked_cast")
    override fun set(thisRef: Any?, property: PropertyMetadata, value: T) {
        this.value = value
        val n = property.name
        when (value) {
            is String -> prefs.edit().putString(n, value).apply()
            is Int -> prefs.edit().putInt(n, value).apply()
            is Long -> prefs.edit().putLong(n, value).apply()
            is Float -> prefs.edit().putFloat(n, value).apply()
            is Boolean -> prefs.edit().putBoolean(n, value).apply()
            is Set<*> -> prefs.edit().putStringSet(n, value as Set<String>).apply()
            else -> throw IllegalArgumentException()
        }

    }

}

