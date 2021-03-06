package kr.re.ec.ashipdalauncher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kr.re.ec.ashipdalauncher.util.LogUtil;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity 
implements OnClickListener, OnCheckedChangeListener  {

	//otherActivity
	private static final int REQUEST_CODE_ANOTHER = 0;
	
	//ClockInfo,RedialInfo
	private SimpleDateFormat mClockFormat, mDateFormat;
	private TextView mClock, mDate, mReDial;
	private Uri uri;
	
	private void initResource() { //now time, date, redial information data set int here
		this.mClock = (TextView)this.findViewById(R.id.btnClock);
		this.mClockFormat = new SimpleDateFormat("HH시 mm분", Locale.getDefault());
		this.mDate = (TextView)this.findViewById(R.id.btnDate);
		this.mDateFormat = new SimpleDateFormat("yyyy년\nMM월 dd일", Locale.getDefault());
		this.mReDial = (TextView)this.findViewById(R.id.btnRedial);
		updateClockTime();
	}

	
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			//action thread
			initResource();
			
			//setting button start!
			
			//setting wifi button
			//ToggleButton wifi=(ToggleButton)findViewById(R.id.btnWifi);
			//wifi.setOnClickListener((OnClickListener)this);
			
			//setting Map button
			Button btnmap = (Button)findViewById(R.id.btnMap);
			btnmap.setOnClickListener((OnClickListener) this);
		
			//setting Date button
			Button btndate=(Button)findViewById(R.id.btnDate);
			btndate.setOnClickListener((OnClickListener) this);
			
			//setting Clock button
			Button btnclock=(Button)findViewById(R.id.btnClock);
			btnclock.setOnClickListener((OnClickListener) this);
			
			//setting Internet button
			Button btninternet=(Button)findViewById(R.id.btnInternet);
			btninternet.setOnClickListener((OnClickListener) this);
			
			//setting Sos button
			Button btnsos=(Button)findViewById(R.id.btnSos);
			btnsos.setOnClickListener((OnClickListener) this);
			
			//setting App button
			Button btnapp=(Button)findViewById(R.id.btnApp);
			btnapp.setOnClickListener((OnClickListener) this);
			
			//setting Message button
			Button btnmms=(Button)findViewById(R.id.btnMms);
			btnmms.setOnClickListener((OnClickListener) this);
			
			//setting Camera button
			Button btncamera=(Button)findViewById(R.id.btnCamera);
			btncamera.setOnClickListener((OnClickListener) this);
			
			//setting Gallery button
			Button btngallery=(Button)findViewById(R.id.btnGallery);
			btngallery.setOnClickListener((OnClickListener) this);
			
			//setting Call button
			Button btnphonecall=(Button)findViewById(R.id.btnPhoneCall);
			btnphonecall.setOnClickListener((OnClickListener) this);
			
			//setting Redial button
			Button btnredial=(Button)findViewById(R.id.btnRedial);
			btnredial.setOnClickListener((OnClickListener) this);
			
			//setting button end!

			//wifi state changed
			//if wifion -> changed ON! wifibtn icon 
			//if(isWifiEnabled(this)){
			//	wifi.setChecked(true);}
			//else{
			//	Log.v("test", "2");
			//	wifi.setChecked(false);
			//}
		}

		//call button click event start !
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnDate:
				Intent it_date = new Intent(Intent.ACTION_MAIN);
				it_date.setComponent(new ComponentName("com.android.calendar","com.android.calendar.LaunchActivity"));
				startActivity(it_date);
				break;

			case R.id.btnClock:
				Intent it_clock= new Intent(Intent.ACTION_MAIN);
				it_clock.setComponent(new ComponentName("com.sec.android.app.alarmclock", "com.sec.android.app.alarmclock.AlarmClock"));
				startActivity(it_clock);
				break;

				/*deprecated
			case R.id.btnWifi:
				final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				// button clicked wifi changed
				wifi.setWifiEnabled(!wifi.isWifiEnabled());
				break;
				*/
			case R.id.btnMap:
				//Intent it_map = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5,127.0"));
				//startActivity(it_map);
				Intent it_magnifier = new Intent(this,Magnifier.class);
				startActivity(it_magnifier);
				LogUtil.v("Clicked");
				break;

			case R.id.btnInternet:
				Intent it_internet = new Intent(Intent.ACTION_MAIN);
				it_internet.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
				startActivity(it_internet);
				break;

			case R.id.btnSos:
				Intent it_Sos = new Intent(this,SosPage.class);
				startActivity(it_Sos);
				break;

			case R.id.btnApp:
				Intent it_app = new Intent(getBaseContext(), AnotherApp.class);
				startActivityForResult(it_app, REQUEST_CODE_ANOTHER);
				break;

			case R.id.btnMms:
				
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.setType("vnd.android-dir/mms-sms");
				startActivity(intent);
				break;

			case R.id.btnCamera:
				Intent it_camera = new Intent (MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
				it_camera.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(it_camera);				
				break;

			case R.id.btnGallery:
				Intent it_gallery=new Intent();
				it_gallery.setAction(Intent.ACTION_VIEW);
 				it_gallery.setData(android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivity(it_gallery);
				break;

			case R.id.btnPhoneCall:
				Intent itPhoneCall = new Intent(Intent.ACTION_DIAL);  
				startActivity(itPhoneCall);  
				break;

			case R.id.btnRedial: 
				Intent itPhoneReDial = new Intent(Intent.ACTION_DIAL);  
				itPhoneReDial.setData(uri);
				startActivity(itPhoneReDial);  
				break;
				
			case R.id.btnSms:
				Intent it_mms = new Intent(Intent.ACTION_MAIN);
				it_mms.setComponent(new ComponentName("com.android.mms","com.android.mms.ui.ConversationComposer"));
				startActivity(it_mms);
				break;
			}
		}
		//call button click event end !

		
		//more apps
		public void onActivityForResult(int requestCode, int resultCode, Intent data){
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == REQUEST_CODE_ANOTHER){
				Toast toast = Toast.makeText(getBaseContext(), "onActivityResult called with Code : " + resultCode,
						Toast.LENGTH_LONG);
				toast.show();
				
				if(resultCode == 1 ){
					String name = data.getExtras().getString("name");
					toast = Toast.makeText(getBaseContext(), "Result name : " +name , Toast.LENGTH_LONG );
					toast.show();
				}
				
			}
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//	Toast.makeText(this,
			//			isChecked ? wifibtn.getTextOn() : wifibtn.getTextOff(),
			//			Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onBackPressed(){
			//super.onBackPressed();
		}
		
		// date, time , redial  data set, and show in window	
		private void updateClockTime(){
			new Thread(){
				public void run(){
					while (true){
						try{
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mClock.setText(mClockFormat.format(new Date()));
									mDate.setText(mDateFormat.format(new Date()));
									//LogUtil.v(mClockFormat.format(new Date()));
									//redial start
									//TODO:if you have time..... query modify to only set last number of my send list.not all phone call list
									try {
										Cursor c = getContentResolver().query(Calls.CONTENT_URI, null, CallLog.Calls.TYPE+"=2", null, Calls.DATE + " DESC");

										if (c != null) {
											if (c.moveToFirst()) {
												c.getString(c.getColumnIndex(Calls.CACHED_NAME));
												
												uri = Uri.parse("tel:" + c.getString(c.getColumnIndex(Calls.NUMBER)));
												// LogUtil.v("uri: " + uri.toString());
											} 
											// Uri is member var
										}
										
										//LogUtil.v(c.getString(c.getColumnIndex(Calls.NUMBER)));
										mReDial.setText(c.getString(c.getColumnIndex(Calls.NUMBER))); 
										//Last num send to btn11(redial btn)

										c.close();
										//end redial

									}
									catch (Exception e) {
										LogUtil.w(e.toString());
									}
								}
							});
							Thread.sleep(700);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();
			}

			//send wifi state 
			public static boolean isWifiEnabled(Context context) {

				WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
				if(wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
					return true;
				} else {
					return false;
				}
			}

}