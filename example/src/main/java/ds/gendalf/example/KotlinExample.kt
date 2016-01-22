package ds.gendalf.example

import android.content.Context

fun doSomePrefs(ctx: Context) {

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


