## Gendalf 
##### Another one library with weird name

This lib produces some sugar and type safety for Android Shared Preferences:

```java
// before
final String ageKey = "age";
final String userNameKey = "userName";
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
```groovy
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

    compile 'ds.gendalf:gendalf:1.3.0'
    apt 'ds.gendalf:compiler:1.3.0'

}
```

Generate prefs model:
```groovy
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
```groovy
Gendalf.with(this)
       .setUserName("Luke");
```

### Custom Type prefs

Add annotation @CustomPref and provide converter class:
```groovy
@PrefsConfig("AppPrefs")
interface AppPrefsConfigurator {
	...
    @CustomPref(GuitarToStringConverter.class) Guitar guitar = null;
}
```

Implement converter:
```groovy
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
```groovy
 AppPrefs.with(this)
         .setGuitar(new Guitar("electric", 99.95f, Color.RED))
```

### License
```
The MIT License (MIT)

Copyright © 2016 Deviant Studio

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
