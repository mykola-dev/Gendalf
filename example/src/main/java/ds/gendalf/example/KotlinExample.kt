package ds.gendalf.example

import android.content.Context
import java.util.*

fun doSomePrefs(ctx: Context) {
    ctx.prefs()
            .setAdmin(true)
            .setCity("Kyiv")
            .setFriends(setOf("Billy", "Jimmy"))
            .setTime(Date().time)
            .apply()

    // kotlin bonus
    KotlinPrefsSetup.init(ctx, "prefs")
    with(KotlinPrefs) {
        name = "vasya"
        age = 20
        sex = "male"
    }

}


public object KotlinPrefs {

    var name by prefsKey("Vasya")
    var age by prefsKey(18)
    var sex by prefsKey("")

}


