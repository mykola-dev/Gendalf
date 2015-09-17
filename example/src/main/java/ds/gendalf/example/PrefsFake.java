package ds.gendalf.example;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class PrefsFake {

	private SharedPreferences prefs;
	private SharedPreferences.Editor edit;


	private PrefsFake(Context ctx) {
		prefs = ctx.getSharedPreferences("main_prefs", Context.MODE_PRIVATE);
		edit = prefs.edit();
	}


	public static PrefsFake with(Context ctx) {
		return new PrefsFake(ctx);
	}


	public String getUserName() {
		return prefs.getString("userName", null);
	}


	public PrefsFake setUserName(String userName) {
		edit.putString("userName", userName);
		return this;
	}


	public PrefsFake setAge(int age) {
		edit.putInt("age", age);
		return this;
	}


	public void apply() {
		edit.apply();
	}


	public void commit() {
		edit.commit();
	}


	public Map<String, ?> getAll() {
		return prefs.getAll();
	}

}
