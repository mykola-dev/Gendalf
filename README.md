### Another one library with weird name
This lib produces some sugar and type safety for android shared preferences:

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

### How to use:


Add gradle dependency:
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:+'
    }
}

apply plugin: 'com.neenbedankt.android-apt'

dependencies {

    compile 'ds.gendalf:gendalf:1.0.0beta3'
    apt 'ds.gendalf:compiler:1.0.0beta3'

}
```

Generate prefs model:
```
@PrefsConfig("Gendalf")
public interface PrefsConfigurator {

	@PrefKey String city = "Kharkiv";
	@PrefKey String userName = null;
	@PrefKey int age = 18;
	@PrefKey int KEY_VERSION = 0;
	@PrefKey boolean admin = true;
	@PrefKey float length = 20.5f;
	@PrefKey long time = 0;
	@PrefKey Set<String> friends = null;
}
```

Run 'make' and then you can use generated helper like
```
Gendalf.with(this)
       .setUserName("Luke")
       .commit();
```
