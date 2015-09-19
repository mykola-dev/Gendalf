package ds.gendalf.example;


import ds.gendalf.PrefKey;
import ds.gendalf.PrefsConfig;

import java.util.Set;

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
