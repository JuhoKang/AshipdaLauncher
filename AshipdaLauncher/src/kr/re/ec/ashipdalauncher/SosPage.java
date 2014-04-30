package kr.re.ec.ashipdalauncher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.ashipdalauncher.*;
import kr.re.ec.ashipdalauncher.util.LogUtil;
import kr.re.ec.ashipdalauncher.dbhelper.SosDataHelper;
import kr.re.ec.ashipdalauncher.objects.Sos;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SosPage extends Activity {
	ArrayList<String> items;
	ArrayAdapter<String> adapter;
	ListView listView;
	Dialog mDialog;
	AlertDialog aDia;
	LayoutInflater inflater;
	TextView text;
	Context Context;
	EditText name;
	EditText editName;
	EditText editNum;
	String eNa;
	String eNu;
	LocationManager locationManager;
	Location location;
	String mAddressStr;

	final static int DIALOG_1 = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sosmain);
		items = new ArrayList<String>();
		SosDataHelper db = new SosDataHelper(this);
		//LogUtil.v("delted");
		//for(int i = 0; i < 20; i++)
		//{
		//	Sos sos;
		//	sos = db.getSos(i);
		//	db.deleteSos(sos);			
		//}
		//LogUtil.v("delted");
		checkdb();

		// listView의 항목을 선택하려면 체크박스나 라디오 버튼이 있어야 하므로 아래의 레이아웃을 선택했다.
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		adapter.notifyDataSetChanged();
		listView = (ListView) findViewById(R.id.sosList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(mItemClickListener);
		listView.setOnItemLongClickListener(mItemLongClickListener);
		findViewById(R.id.btnSosAdd).setOnClickListener(mClickListener);
		findViewById(R.id.btnSosEdit).setOnClickListener(mClickListener);

		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.sosadd, null);
		AlertDialog.Builder aDialog = new AlertDialog.Builder(SosPage.this);
		aDialog.setTitle("추가");
		aDialog.setView(layout);
		aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		aDialog.setPositiveButton("추가", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				editName = (EditText) ((AlertDialog) dialog)
						.findViewById(R.id.editSosName);
				editNum = (EditText) ((AlertDialog) dialog)
						.findViewById(R.id.editSosNum);
				eNa = editName.getText().toString();
				eNu = editNum.getText().toString();
				Sos n_sos = new Sos(eNa, eNu);
				SosDataHelper db = new SosDataHelper(SosPage.this);
				db.addSos(n_sos);
				checkdb();
				LogUtil.v(items.get(0));
				adapter.notifyDataSetChanged();
				adapter = new ArrayAdapter<String>(SosPage.this,
						android.R.layout.simple_list_item_1, items);
				listView.setAdapter(adapter);
				listView.invalidateViews();
				LogUtil.v(items.get(0));
			}
		});
		aDia = aDialog.create();

	}

	// 상단 버튼에서 사용하는 listener
	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnSosAdd:

				aDia.show();
				break;
			case R.id.btnSosEdit:
				Intent intent = new Intent(SosPage.this, SosEdit.class);
				startActivity(intent);
				finish();
				break;
			}
		}
	};

	// ListView에서 사용하는 listener
	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		SosDataHelper db = new SosDataHelper(SosPage.this);
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			String name = items.get(position);
			Sos lsos = db.getSosWithName(name);
			String number = lsos.getPhoneNumber();
						
			//gps
			//checkMyLocation();
			//getAddress();
			
			//String message = "메시지";
			//SendSMS(number, message);
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ number));
			startActivity(intent);
			finish();
		}
	};
	
	AdapterView.OnItemLongClickListener mItemLongClickListener = new AdapterView.OnItemLongClickListener() {
		SosDataHelper db = new SosDataHelper(SosPage.this);

		public boolean onItemLongClick(AdapterView parent, View view,
				int position, long id) {
			String name = items.get(position);
			Sos lsos = db.getSosWithName(name);
			String number = lsos.getPhoneNumber();
			checkMyLocation();
			getAddress();
			String message = mAddressStr;
			SendSMS(number, message);

			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ number));
			startActivity(intent);
			finish();

			return false;
		}

	};

	public void checkdb() {
		SosDataHelper db = new SosDataHelper(this);
		items.clear();
		items = db.getAllSosName();		
	}
	
	private void SendSMS(String phonenumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        String sendTo = phonenumber;
        String myMessage = message;
        smsManager.sendTextMessage(sendTo, null, myMessage, null, null);
        Toast.makeText(SosPage.this, "SOS 메시지가 전송되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
	
	public void checkMyLocation(){
		   locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		   
		   Criteria criteria = new Criteria();
		   
		   
		   String provider = locationManager.getBestProvider(criteria, true);
		   
		   locationManager.requestLocationUpdates(provider, 10000, 100, new MyLocationListener());
		   
		   if(provider == null){ //gps off이면 network통해서 받아오도록..
		    Toast.makeText(getBaseContext(), "no GPS Provider", Toast.LENGTH_SHORT).show();
		    provider = LocationManager.NETWORK_PROVIDER;
		    location = locationManager.getLastKnownLocation(provider);
		   }
		   
		   location = locationManager.getLastKnownLocation(provider);
		   
		   if(location == null){
		    try{
		     Thread.sleep(10000);
		    }catch(InterruptedException e){
		     e.printStackTrace();
		    }
		    location = locationManager.getLastKnownLocation(provider);    
		   }
		  }
		  
		  private class MyLocationListener implements LocationListener{

		   @Override
		   public void onLocationChanged(Location location) {
		    // TODO Auto-generated method stub
		    SosPage.this.location = location;
		   }

		   @Override
		   public void onProviderDisabled(String provider) {
		    // TODO Auto-generated method stub
		    
		   }

		   @Override
		   public void onProviderEnabled(String provider) {
		    // TODO Auto-generated method stub
		    
		   }

		   @Override
		   public void onStatusChanged(String provider, int status,
		     Bundle extras) {
		    // TODO Auto-generated method stub
		    
		   }
		   
		  }
		  
		  public void getAddress() {
			  Geocoder geoCoder = new Geocoder(this);
			  double lat = location.getLatitude();
			  double lng = location.getLongitude();
			  
			  List<Address> addresses = null;
			  try {
			   addresses = geoCoder.getFromLocation(lat, lng, 5);
			  } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			  }
			  if(addresses.size()>0){
			   Address mAddress = addresses.get(0);
			   //String Area = mAddress.getCountryName();
			   mAddressStr = "SOS콜 이 핸드폰은 지금 "+mAddress.getCountryName()+" "
			       +mAddress.getLocality()+" "
			       +mAddress.getThoroughfare()+" "
			       +mAddress.getFeatureName()+" 근처에 있습니다 ";
			   Toast.makeText(getBaseContext(), mAddressStr, Toast.LENGTH_SHORT).show();
			  }
			 }



}
