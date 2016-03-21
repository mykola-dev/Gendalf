package ds.gendalf.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Arrays;

import ds.gendalf.example.data.Guitar;

import static ds.gendalf.example.data.Direction.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android style
        final String idKey = "ID";
        final String ageKey = "age";
        final String userNameKey = "userName";
        final String adminKey = "admin";
        final String cityKey = "city";
        SharedPreferences userSharedPrefs = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSharedPrefs.edit();
        editor.putInt(idKey, 100500);
        editor.putInt(ageKey, 12);
        editor.putString(userNameKey, "Luke");
        editor.putString(cityKey, "Kharkiv");
        editor.putBoolean(adminKey, true);
        editor.apply();

        final String osKey = "os";
        final String versionKey = "version";
        final String secretKey = "PREF_SECRET";
        SharedPreferences appSharedPrefs = this.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        editor = appSharedPrefs.edit();
        if (appSharedPrefs.contains(osKey)) {
            editor.clear();
            editor.putString(osKey, "Windows");
            editor.putString(versionKey, "1995");
            editor.putString(secretKey, "00000000");
            editor.apply();
        }

        // Gendalf style
        UserPrefs.with(this)
                 .setId(100500)
                 .setAge(12)
                 .setUserName("Luke")
                 .setCity(null)
                 .setAdmin(true);

        AppPrefs appPrefs = AppPrefs.with(this);

        appPrefs.clearAll()
          .setOs("Windows")
          .setVersion("1995")
          .setNames(Arrays.asList("Geralt", "Triss", "Yennefer"))
          .setDirections(Arrays.asList(UP, UP, DOWN, DOWN, LEFT, RIGHT, LEFT, RIGHT))
          .setGuitar(new Guitar("electric", 99.95f, Color.RED))
          .setPrefSecret("00000000");

        Log.v("directions", appPrefs.fetchDirections().toString());

        // direct prefs access
        SharedPreferences rawPrefs = appPrefs.getPrefs();

        appPrefs.applyDefaults();

        Log.v("json guitar",appPrefs.fetchGuitar().type);

        Log.v("all keys", AppPrefs.KEYS.toString());
        Log.v("colors type", AppPrefs.KEYS.get(AppPrefs.KEY_COLORS).getSimpleName());


    }

}