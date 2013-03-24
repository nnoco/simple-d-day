package com.nnoco.dday.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotiUpdateService extends Service {
	Notification notification;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		int superValue = super.onStartCommand(intent, flags, startId);
		
//		intent.±ÍÂ÷³¶..
		
		
		return superValue;
	}

}
