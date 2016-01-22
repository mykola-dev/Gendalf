package ds.gendalf.example;


import java.util.Set;

import ds.gendalf.PrefKey;
import ds.gendalf.PrefsConfig;

@PrefsConfig(filename = "app_prefs", cls = "AppPrefs")
interface AppPrefsConfigurator {

    @PrefKey String version = null;
    @PrefKey String os = null;
}

@PrefsConfig(filename = "user_prefs", cls = "UserPrefs")
interface UserPrefsConfigurator {

    @PrefKey String city = "Kharkiv";
    @PrefKey String userName = null;
    @PrefKey int age = 18;
    @PrefKey int KEY_RATE = 0;
    @PrefKey boolean admin = true;
    @PrefKey float length = 20.5f;
    @PrefKey long time = 0;
    @PrefKey Set<String> friends = null;
}
