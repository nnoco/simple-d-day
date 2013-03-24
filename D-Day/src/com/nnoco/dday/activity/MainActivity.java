package com.nnoco.dday.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnDateChangedListener, OnCheckedChangeListener {
	public static final String PREFS_NAME = "PrefFile";
	
	private EditText etTitle;
	private CheckBox chkDisplayNoti;
	private DatePicker datePicker;
	private TextView tvDDay;
	private NotificationManager notiMgr;
	
	private int dYear;
	private int dMonth;
	private int dDay;
	
	private boolean isDisplayNoti;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        notiMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        etTitle = (EditText)findViewById(R.id.etTitle);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        chkDisplayNoti = (CheckBox)findViewById(R.id.chkDisplayNoti);
        tvDDay = (TextView)findViewById(R.id.tvDDay);
        
        loadPrefs();
        
        chkDisplayNoti.setOnCheckedChangeListener(this);
        tvDDay.setText(getDDayString());
    }
    
    private void loadPrefs(){
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	
    	String title = etTitle.getText().toString();
    	dYear = datePicker.getYear();
    	dMonth = datePicker.getMonth();
    	dDay = datePicker.getDayOfMonth();
    	isDisplayNoti = chkDisplayNoti.isChecked();
    	
    	title = settings.getString("title", title);
    	dYear = settings.getInt("dYear", dYear);
    	dMonth = settings.getInt("dMonth", dMonth);
    	dDay = settings.getInt("dDay", dDay);
    	isDisplayNoti = settings.getBoolean("displayNoti", isDisplayNoti);
    	
    	etTitle.setText(title);
    	datePicker.init(dYear, dMonth, dDay, this);
    	chkDisplayNoti.setChecked(isDisplayNoti); 	
    	
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	
    	String title = etTitle.getText().toString();
    	
    	editor.putString("title", title);
    	editor.putInt("dYear", dYear);
    	editor.putInt("dMonth", dMonth);
    	editor.putInt("dDay", dDay);
    	editor.putBoolean("displayNoti", isDisplayNoti);
    	
    	editor.commit();
    }
    
    private String getDDayString(){
    	Calendar calendar = Calendar.getInstance();
		long nowDay = calendar.getTimeInMillis() / 86400000;
		calendar.set(dYear, dMonth, dDay);
		long diff = calendar.getTimeInMillis() / 86400000;
		
		return String.format("D%+d", nowDay-diff);
    }

    private void noti(){
		String title = etTitle.getText().toString();
		String strDDay = getDDayString();
		String notiMsg = title + " " + strDDay;
		
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		Notification notification = new Notification(R.drawable.ic_dday_small, notiMsg, System.currentTimeMillis());
		notification.setLatestEventInfo(MainActivity.this, strDDay, title, pi);
		notification.flags = Notification.FLAG_NO_CLEAR;
		
		notiMgr.notify(0, notification);
    }
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		dYear = year;
		dMonth = monthOfYear;
		dDay = dayOfMonth;
		
		tvDDay.setText(getDDayString());
		
		if(isDisplayNoti){
			noti();
		}
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		isDisplayNoti = isChecked;
		if(isChecked){
			noti();
		}else{
			notiMgr.cancel(0);
		}
	}
}
