package ds.gendalf.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// before
		SharedPreferences prefs = getSharedPreferences("main_prefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("age", 12);
		editor.putString("userName", "Mykola");
		editor.apply();

		// after
		PrefsFake.with(this)
		         .setAge(12)
		         .setUserName("Mykola")
		         .apply();

		// generated
		Gendalf.with(this)
		       .apply();


	}

}