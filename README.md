## Gendalf 
##### Another one library with weird name

This lib produces some sugar and type safety for Android Shared Preferences:

```
// before
final String ageKey = "age";
final String userNameKey = "age";
final String adminKey = "admin";
SharedPreferences prefs = getSharedPreferences("custom_prefs", Context.MODE_PRIVATE);
SharedPreferences.Editor editor = prefs.edit();
editor.putInt(ageKey, 12);
editor.putString(userNameKey, "Luke");
editor.putBoolean(adminKey,true);
editor.apply();

// after
Gendalf.with(this)
       .setAge(12)
       .setUserName("Luke")
       .setAdmin(true);
```

### Basic usage

Add gradle dependency:
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}

apply plugin: 'com.neenbedankt.android-apt'

dependencies {

    compile 'ds.gendalf:gendalf:1.1.0'
    apt 'ds.gendalf:compiler:1.1.0'

}
```

Generate prefs model:
```
@PrefsConfig("Gendalf")
public interface PrefsConfigurator {
	String city = "Kharkiv";
	String userName = null;
	int age = 18;
	int KEY_VERSION = 0;
	boolean admin = true;
	float length = 20.5f;
	long time = 0;
	Set<String> friends = null;
}
```

Run 'make' and then you can use generated helper like
```
Gendalf.with(this)
       .setUserName("Luke")
       .commit();
```

### Custom Type prefs

Add annotation @CustomPref and provide converter class:
```
@PrefsConfig("AppPrefs")
interface AppPrefsConfigurator {
	...
    @CustomPref(GuitarToStringConverter.class) Guitar guitar = null;
}
```

Implement converter:
```
public class GuitarToStringConverter implements Converter<Guitar, String> {
    @Override
    public String serialize(Guitar g) {
        return String.format("%s~%s~%s", g.type, g.price, g.color);
    }

    @Override
    public Guitar deserialize(String prefValue) {
        String[] splits = prefValue.split("~");
        return new Guitar(splits[0], Float.valueOf(splits[1]), Integer.valueOf(splits[2]));
    }
}
```
Actually you can use any serializer. For example GSON.

Now you can use prefs like:
```
 AppPrefs.with(this)
         .setGuitar(new Guitar("electric", 99.95f, Color.RED))
```