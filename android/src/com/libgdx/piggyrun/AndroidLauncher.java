package com.libgdx.piggyrun;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.libgdx.piggyrun.utils.GamePreferences;

public class AndroidLauncher extends AndroidApplication {
	public static AlarmManager am;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new PiggyRunMain(), config);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		AlarmReceiver.nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
		AlarmReceiver.nm.cancelAll();
		AlarmReceiver.inGame = true;
	}

	@Override
	protected void onPause() {
		AlarmReceiver.inGame = false;
		Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
		intent.putExtra("enable", GamePreferences.instance.notificationsEnable);
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + AlarmManager.INTERVAL_HOUR * 8,AlarmManager.INTERVAL_HOUR * 8,
				PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT));
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		AlarmReceiver.inGame = true;
		AlarmReceiver.nm.cancelAll();
	}
}
