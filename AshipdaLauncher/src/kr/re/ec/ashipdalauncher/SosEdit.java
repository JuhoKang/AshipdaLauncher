package kr.re.ec.ashipdalauncher;

import java.util.ArrayList;

import kr.re.ec.ashipdalauncher.R;
import kr.re.ec.ashipdalauncher.dbhelper.SosDataHelper;
import kr.re.ec.ashipdalauncher.objects.Sos;
import kr.re.ec.ashipdalauncher.util.LogUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SosEdit extends Activity {
	ArrayList<String> items;
	ArrayAdapter<String> adapter;
	ListView listView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sosedit);
		items = new ArrayList<String>();
		checkdb();
		LogUtil.v("뿌잉");
		// listView의 항목을 선택하려면 체크박스나 라디오 버튼이 있어야 하므로 아래의 레이아웃을 선택했다.
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, items);
		listView = (ListView) findViewById(R.id.sosEditList);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		LogUtil.v("뿌잉");
		int i;
		LogUtil.v("뿌잉");
		listView.setOnItemClickListener(mItemClickListener);
		findViewById(R.id.btnEditDelete).setOnClickListener(mClickListener);
		findViewById(R.id.btnEditCancel).setOnClickListener(mClickListener);
	}

	// 상단 버튼에서 사용하는 listener
	Button.OnClickListener mClickListener = new View.OnClickListener() {

		public void onClick(View v) {
			SosDataHelper db = new SosDataHelper(SosEdit.this);
			switch (v.getId()) {

			case R.id.btnEditDelete:

				int pos = listView.getCheckedItemPosition();
				LogUtil.v("shit" + String.valueOf(pos));
				if (pos != ListView.INVALID_POSITION) {
					LogUtil.i(items.get(pos));
					LogUtil.v("" + pos);
					String name = items.get(pos);

					LogUtil.v("" + pos);
					Sos eSos;
					LogUtil.v("" + pos);
					eSos = db.getSosWithName(name);
					LogUtil.i(items.get(pos));
					db.deleteSos(eSos);
					LogUtil.i(items.get(pos));
					items.remove(pos);
					LogUtil.i(items.get(pos));
					LogUtil.v("" + pos);

					LogUtil.v("" + pos);

					adapter.notifyDataSetChanged();
					Intent intent = new Intent(SosEdit.this,
							SosPage.class);
					startActivity(intent);
					finish();
				}
				break;
			case R.id.btnEditCancel:
				Intent intent = new Intent(SosEdit.this, SosPage.class);
				startActivity(intent);
				finish();
				break;
			}
		}
	};

	// ListView에서 사용하는 listener
	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			String mes = "Select item = " + items.get(position);
			Toast.makeText(SosEdit.this, mes, Toast.LENGTH_SHORT).show();
		}
	};
	
	public void checkdb() {
		SosDataHelper db = new SosDataHelper(this);
		items.clear();
		items = db.getAllSosName();
		items.add("");
	}
}

