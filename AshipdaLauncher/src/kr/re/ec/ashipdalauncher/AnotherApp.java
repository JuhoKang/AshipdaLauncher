package kr.re.ec.ashipdalauncher;


import java.util.ArrayList;
import java.util.List;

import kr.re.ec.ashipdalauncher.util.LogUtil;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AnotherApp extends Activity {
	ListView lv;
	ListView lv2;
	EditText editText;
	MyAdapter adapter;
	
	public static int pos[] = new int[100];
	public static int p;
	public static List<ResolveInfo> pkgAppsList; 
	public static List<ResolveInfo> mostList;
	@Override
	public void onResume(){
//		super.onCreate(savedInstanceState);
		super.onResume();
		setContentView(R.layout.activity_another_apps);
		
		//Get All apps
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		pkgAppsList = getPackageManager().queryIntentActivities( mainIntent, 0);
		adapter = new MyAdapter(this, R.layout.listitem, pkgAppsList);
		lv = (ListView) findViewById(R.id.applistview);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				pos[position] += 1;
				getCount();
				LogUtil.v("onitem"+pos[p]);
				
				ResolveInfo temp = pkgAppsList.get(position);
				Intent i = new Intent();
				i.setClassName(temp.activityInfo.packageName, temp.activityInfo.name);
				startActivity(i);
				onResume();
			}
		});
		
		
		editText = (EditText)this.findViewById(R.id.SearchListView);
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				List<ResolveInfo> edList = new ArrayList<ResolveInfo>(pkgAppsList);
				
				EdAdapter edAdapter = new EdAdapter(AnotherApp.this , R.layout.listitem , pkgAppsList);
				EdAdapter resEdAdapter = new EdAdapter(AnotherApp.this , R.layout.listitem , edList);
				for(int i=0;i<pkgAppsList.size();i++){ 
				    ResolveInfo getData = pkgAppsList.get(i);
				    //문자열검사
				    if(!(getData.loadLabel(getPackageManager()).toString().toUpperCase().contains(arg0) || getData.loadLabel(getPackageManager()).toString().toLowerCase().contains(arg0))
				    		&& !arg0.toString().isEmpty() ){
				    	resEdAdapter.remove(getData);
				    }
				    	
				}
				
				if(arg0.toString().isEmpty()){
					resEdAdapter = edAdapter;
				}
				lv.setAdapter(resEdAdapter);
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});


		
		ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        final List<RecentTaskInfo> task = am.getRecentTasks(2,0);
	    lv2 = (ListView) findViewById(R.id.recentView);
		RecentAdapter ad = new RecentAdapter(this,android.R.layout.simple_list_item_1,task);
		lv2.setAdapter(ad);
		lv2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv2.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent,View v,int position,long id){
				RecentTaskInfo tk = task.get(position+1);
				Intent it = tk.baseIntent;
				startActivity(it);
			}
		});
		
		mostList = new ArrayList<ResolveInfo>(pkgAppsList);
		MostAdapter mostadapter = new MostAdapter(this, R.layout.listitem, mostList);
		ListView lv3 = (ListView) findViewById(R.id.mostView);
		lv3.setAdapter(mostadapter);

		lv3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ResolveInfo temp = mostList.get(position);
				Intent i = new Intent();
				i.setClassName(temp.activityInfo.packageName, temp.activityInfo.name);
				startActivity(i);
				onResume();
			}
		});
		
		
	}
	
	//ListView Adapter
	public class MyAdapter extends ArrayAdapter<ResolveInfo> {

		List<ResolveInfo> child;

		public MyAdapter(Context context, int textViewResourceId, List<ResolveInfo> objects) {
			super(context, textViewResourceId, objects);
			child = objects;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return child.size();
		}

		@Override
		public ResolveInfo getItem(int position) {
			// TODO Auto-generated method stub
			return child.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.listitem, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.ivIcon);
			TextView name = (TextView) convertView.findViewById(R.id.tvName);

			ResolveInfo temp = child.get(position);
			icon.setImageDrawable(temp.loadIcon(getPackageManager()));
			name.setText(temp.loadLabel(getPackageManager()));


			return convertView;
		}

	}
	
	
	
	public class MostAdapter extends ArrayAdapter<ResolveInfo> {

		List<ResolveInfo> child;
		
		public MostAdapter(Context context, int textViewResourceId, List<ResolveInfo> objects) {
			super(context, textViewResourceId, objects);
			child = objects;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public ResolveInfo getItem(int position) {
			// TODO Auto-generated method stub
			return child.get(p);
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			if(convertView == null) {
				LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.listitem, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.ivIcon);
			TextView name = (TextView) convertView.findViewById(R.id.tvName);
			
			
			child.set(0, child.get(p));
			ResolveInfo temp = child.get(0);
			icon.setImageDrawable(temp.loadIcon(getPackageManager()));
			name.setText(temp.loadLabel(getPackageManager()));


			return convertView;
		}

	}
	
	
	//건드릴 필요 없음
	public class EdAdapter extends ArrayAdapter<ResolveInfo> {

		List<ResolveInfo> child;
		int position;
		public EdAdapter(Context context, int textViewResourceId, List<ResolveInfo> objects) {
			super(context, textViewResourceId, objects);
			child = objects;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return child.size();
		}

		@Override
		public ResolveInfo getItem(int position) {
			// TODO Auto-generated method stub
			return child.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.listitem, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.ivIcon);
			TextView name = (TextView) convertView.findViewById(R.id.tvName);

			ResolveInfo temp = child.get(position);
			icon.setImageDrawable(temp.loadIcon(getPackageManager()));
			name.setText(temp.loadLabel(getPackageManager()));


			return convertView;
		}

	}

	//최근 사용한 앱 어댑터
	public class RecentAdapter extends ArrayAdapter<RecentTaskInfo>{
		List<RecentTaskInfo> child;

		public RecentAdapter(Context context, int textViewResourceId, List<RecentTaskInfo> objects) {
			super(context, textViewResourceId, objects);
			child = objects;
		}		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return child.size()-1;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.listitem, null);
			}
			PackageManager manager = getPackageManager();
			Intent intent = child.get(position+1).baseIntent;
			ResolveInfo temp = manager.resolveActivity(intent, 0);
			
			ImageView icon = (ImageView) convertView.findViewById(R.id.ivIcon);
			TextView name = (TextView) convertView.findViewById(R.id.tvName);   
			
			icon.setImageDrawable(temp.loadIcon(getPackageManager()));
			name.setText(temp.loadLabel(getPackageManager()));
			
			return convertView;
		}
		
		

	}

	void getCount(){
		for(int j=0;j<pos.length;j++){
			if(pos[p] < pos[j]){
				p = j;
				LogUtil.v("Get"+p);
			}
		}
	}

}
