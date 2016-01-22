package ds.gendalf.example;


import java.util.Set;

import ds.gendalf.PrefKey;
import ds.gendalf.PrefsConfig;

@PrefsConfig("AppPrefs")
interface AppPrefsConfigurator {

    @PrefKey String version = null;
    @PrefKey String os = null;
    @PrefKey String PREF_SECRET = "12345678";
}

@PrefsConfig("UserPrefs")
interface UserPrefsConfigurator {

    @PrefKey String city = "Kharkiv";
    @PrefKey String userName = null;
    @PrefKey int age = 18;
    @PrefKey int KEY_RATE = 0;
    @PrefKey int ID = -1;
    @PrefKey boolean admin = true;
    @PrefKey float length = 20.5f;
    @PrefKey long time = 0;
    @PrefKey Set<String> friends = null;
}
