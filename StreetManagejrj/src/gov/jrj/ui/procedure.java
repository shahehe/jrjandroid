package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.ui.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class procedure extends Activity {

	String tel;
	TextView mTextTitle;
	String prefix;
	ListView lstView;
	String subTitle = "", subject = "";
	String[] procedureTitles;

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
		int arrayRes = getIntent().getExtras().getInt(Constants.KEY_ARRAY);
		if (arrayRes == R.array.gongshang_array)
			prefix = "gongshang";
		else
			prefix = "procedure";

		// procedureTitles = res.getStringArray(R.array.procedure_array);
		procedureTitles = res.getStringArray(arrayRes);

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < procedureTitles.length; i++) {
			HashMap<String, Object> temp = new HashMap<String, Object>();
			temp.put("subject", procedureTitles[i]);
			temp.put("ic_operate", R.drawable.gev_info_dis);

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

			
					// Intent intent = new Intent();
					Intent intent = new Intent(procedure.this,
							Government_Introduction.class);
					// intent.setClass(this,Government_Introduction.class);
					intent.putExtra(Constants.KEY_SUBJECT_NAME, subject);
					intent.putExtra(Constants.KEY_ITEM_NAME, subTitle);
					intent.putExtra(Constants.SUBJECT_TITLE,
							procedureTitles[arg2]);
					intent.putExtra(Constants.CONTENT_FILE,
							prefix + Integer.toString(arg2));
					startActivity(intent);
				
			}

		});
		
		try{
			int second = Integer.parseInt(getIntent().getStringExtra("second"));
			second--;
			Intent intent = new Intent(procedure.this,
					Government_Introduction.class);
			// intent.setClass(this,Government_Introduction.class);
			intent.putExtra(Constants.KEY_SUBJECT_NAME, subject);
			intent.putExtra(Constants.KEY_ITEM_NAME, subTitle);
			intent.putExtra(Constants.SUBJECT_TITLE,
					procedureTitles[second]);
			intent.putExtra(Constants.CONTENT_FILE,
					prefix + Integer.toString(second));
			startActivity(intent);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private void setTitle() {

		subTitle = getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
		subject = getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
		mTextTitle.setText(subject + " > " + subTitle);

	}

	private void setBackBtnLisenter() {
		Button backBtn = (Button) findViewById(R.id.about_back_btn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				procedure.this.finish();
			}
		});
	}

}
