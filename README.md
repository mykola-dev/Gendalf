# Gendalf

Add some sugar and type safety to android shared prefs:

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
		       .setAdmin(true)
		       .apply();
```

## How to use:


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
repositories {
    jcenter()
    maven { url "https://dl.bintray.com/deviant-studio/maven/" }
}
apply plugin: 'com.neenbedankt.android-apt'

dependencies {

    compile 'ds.gendalf:gendalf:+'
    apt 'ds.gendalf:compiler:+'

}
```

Generate prefs model:
```
@PrefsConfig("custom_prefs")
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

After run build process Annotations Processor should generate Gendalf class. You can then use it like
```
	Gendalf.with(this)
		       .setUserName("Luke")
		       .commit();
```
