package gov.jrj.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gov.jrj.library.http.Alerts;
import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.Config;
import gov.jrj.library.http.ImageLoader;
import gov.jrj.library.http.JsonHttpResponseHandler;
import gov.jrj.library.http.Session;

import gov.jrj.R;
import gov.jrj.R.array;
import gov.jrj.ui.ProductListActivity.ListItemAdapter;
import gov.jrj.ui.util.Constants;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HealthViewActivity extends Activity {

	String tel;
	TextView mTextTitle;
	String prefix;
	ListView lstView;
	String subTitle = "", subject = "";
	String[] procedureTitles;
	String[] healthUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.title_list);

		mTextTitle = (TextView) findViewById(R.id.txt_title);
		lstView = (ListView) findViewById(R.id.list);

		setTitle();
		setBackBtnLisenter();

		Resources res = getResources();
		// int arrayRes = getIntent().getExtras().getInt(Constants.KEY_ARRAY);
		// if (arrayRes == R.array.gongshang_array)
		// prefix = "gongshang";
		// else
		// prefix = "procedure";
		//
		procedureTitles = res.getStringArray(R.array.health_array);
		healthUrl = res.getStringArray(R.array.health_url);
		try {
			Bundle bundle = getIntent().getExtras();
			int num = bundle.getInt("number");
			switch (num) {
			case 1:
				procedureTitles = res.getStringArray(R.array.health_item_1);
				healthUrl = res.getStringArray(R.array.health_url_1);
				break;
			case 2:
				procedureTitles = res.getStringArray(R.array.health_item_2);
				healthUrl = res.getStringArray(R.array.health_url_2);
				break;
			case 3:
				procedureTitles = res.getStringArray(R.array.health_item_3);
				healthUrl = res.getStringArray(R.array.health_url_3);
				break;
			case 4:
				procedureTitles = res.getStringArray(R.array.health_item_4);
				healthUrl = res.getStringArray(R.array.health_url_4);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// procedureTitles = res.getStringArray(arrayRes);

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < procedureTitles.length; i++) {
			HashMap<String, Object> temp = new HashMap<String, Object>();
			temp.put("subject", procedureTitles[i]);
			temp.put("ic_operate", R.drawable.health);

			listItem.add(temp);
		}
		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,
				listItem,// 数据源
				R.layout.listview_item,
				new String[] { "subject", "ic_operate" }, new int[] {
						R.id.text_summary, R.id.ic_operatate });

		// 添加并且显示
		lstView.setAdapter(listItemAdapter);
		/*
		 * //生成适配器的Item和动态数组对应的元素 SimpleAdapter listItemAdapter = new
		 * SimpleAdapter(this,listItem,//数据源
		 * R.layout.contact_phone_item,//ListItem的XML实现 //动态数组与ImageItem对应的子项
		 * new String[] {"subject","ic_operate", "Image"},
		 * //ImageItem的XML文件里面的一个ImageView,两个TextView ID new int[]
		 * {R.id.item_text,R.id.item_text_describe,R.id.call_out} );
		 * 
		 * //添加并且显示 lstView.setAdapter(listItemAdapter);
		 */
		// 添加点击

		lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (healthUrl[arg2].equals("h01.htm")) {
					// Intent intent = new Intent();
					Intent intent = new Intent(HealthViewActivity.this,
							HealthInfoActivity.class);
					// intent.setClass(this,Government_Introduction.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", procedureTitles[arg2]);
					bundle.putString("url", "file:///android_asset/"
							+ healthUrl[arg2]);
					// intent.putExtra("title", subject);
					// intent.putExtra(Constants.KEY_ITEM_NAME, subTitle);
					// intent.putExtra(Constants.SUBJECT_TITLE,
					// procedureTitles[arg2]);
					// intent.putExtra(Constants.CONTENT_FILE,
					// prefix + Integer.toString(arg2));
					intent.putExtras(bundle);
					startActivity(intent);
				} else if (healthUrl[arg2].contains("htm")) {
					Intent intent = new Intent(HealthViewActivity.this,
							HealthInfoActivity.class);
					// intent.setClass(this,Government_Introduction.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", procedureTitles[arg2]);
					bundle.putString("url", "file:///android_asset/"
							+ healthUrl[arg2]);
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					Intent intent = new Intent(HealthViewActivity.this,
							HealthViewActivity.class);
					// intent.setClass(this,Government_Introduction.class);
					Bundle bundle = new Bundle();
					bundle.putInt("number", arg2);
					intent.putExtras(bundle);
					startActivity(intent);
				}

			}

		});
		//
		// try{
		// int second = Integer.parseInt(getIntent().getStringExtra("second"));
		// second--;
		// Intent intent = new Intent(HealthViewActivity.this,
		// Government_Introduction.class);
		// // intent.setClass(this,Government_Introduction.class);
		// intent.putExtra(Constants.KEY_SUBJECT_NAME, subject);
		// intent.putExtra(Constants.KEY_ITEM_NAME, subTitle);
		// intent.putExtra(Constants.SUBJECT_TITLE,
		// procedureTitles[second]);
		// intent.putExtra(Constants.CONTENT_FILE,
		// prefix + Integer.toString(second));
		// startActivity(intent);
		// }catch(Exception e){
		// e.printStackTrace();
		// }

	}

	private void setTitle() {

		// subTitle =
		// getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
		// subject =
		// getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
		mTextTitle.setText("健康服务");

	}

	private void setBackBtnLisenter() {
		Button backBtn = (Button) findViewById(R.id.about_back_btn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				HealthViewActivity.this.finish();
			}
		});
	}

}