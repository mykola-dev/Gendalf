package ds.gendalf.example

import android.app.Application
import com.facebook.stetho.Stetho

public class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}
