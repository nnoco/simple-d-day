package com.nnoco.dday.receiver;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.nnoco.dday.activity.MainActivity;
import com.nnoco.dday.activity.R;

public class StartUpReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		loadPrefs(context);
	}

	private void loadPrefs(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				MainActivity.PREFS_NAME, 0);
		boolean isDisplayNoti = settings.getBoolean("displayNoti", false);

		if (isDisplayNoti) {
			String title = settings.getString("title", "");
			Calendar calendar = Calendar.getInstance();
			int dYear = calendar.get(Calendar.YEAR);
			int dMonth = calendar.get(Calendar.MONTH);
			int dDay = calendar.get(Calendar.DAY_OF_MONTH);

			dYear = settings.getInt("dYear", dYear);
			dMonth = settings.getInt("dMonth", dMonth);
			dDay = settings.getInt("dDay", dDay);

			NotificationManager notiMgr = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			long nowDay = calendar.getTimeInMillis() / 86400000;
			calendar.set(dYear, dMonth, dDay);
			long diff = calendar.getTimeInMillis() / 86400000;
			String strDDay = String.format("D%+d", nowDay - diff);

			Notification notification = new Notification(
					R.drawable.ic_dday_small, title + " " + strDDay,
					System.currentTimeMillis());

			notification.flags = Notification.FLAG_NO_CLEAR;
			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			notification.setLatestEventInfo(context, strDDay, title, pi);

			notiMgr.notify(0, notification);
			
			Toast.makeText(context, "Simple D-Day 알림이 등록되었습니다.", Toast.LENGTH_SHORT).show();
		}
	}

}
