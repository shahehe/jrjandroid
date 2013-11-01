package gov.jrj.ui;

import java.util.ArrayList;
import java.util.List;

import gov.jrj.R;
import gov.jrj.ui.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gov.jrj.library.http.Alerts;
import gov.jrj.library.http.Config;
import gov.jrj.library.http.JSONRPCHandler;
import gov.jrj.library.http.JSONRPCService;
import gov.jrj.library.http.Session;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AwardListActivity extends Activity {

	private ProgressDialog pd;
	private Activity mContext;

	String[] itemTitle;

	ProgressBar progressBar;
	TextView mTextTitle;
	ListView listView;
	Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.award_list_activity);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		listView = (ListView) findViewById(R.id.list);
		btnBack = (Button) findViewById(R.id.about_back_btn);
		itemTitle = getResources().getStringArray(R.array.award);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE);
		initViews();
		// this.setTitle();
	}

	// private void setTitle() {
	// String subTitle = "", subject = "";
	// subTitle = getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
	// subject = getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
	// mTextTitle.setText(subject + " > " + subTitle);
	//
	// }

	private void initViews() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					Intent intent = new Intent(AwardListActivity.this,
							ProductInfoActivity.class);
					intent.putExtra("data", arg2);
					startActivity(intent);
				} catch (Exception ex) {

				}
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AwardListActivity.this.finish();
			}
		});
		listView.setAdapter(new ListItemAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (arg2) {
				case 0:
					intent.setClass(mContext, Government_Introduction.class);
					intent.putExtra(Constants.KEY_SUBJECT_NAME,
							getString(R.string.tab1));
					intent.putExtra(Constants.KEY_ITEM_NAME, (itemTitle[0]));
					intent.putExtra(Constants.SUBJECT_TITLE, (itemTitle[0]));
					intent.putExtra(Constants.CONTENT_FILE, "award1.txt");
					mContext.startActivity(intent);
					break;
				case 1:
					intent.setClass(mContext, Government_Introduction.class);
					intent.putExtra(Constants.KEY_SUBJECT_NAME,
							getString(R.string.tab1));
					intent.putExtra(Constants.KEY_ITEM_NAME, (itemTitle[1]));
					intent.putExtra(Constants.SUBJECT_TITLE, (itemTitle[1]));
					intent.putExtra(Constants.CONTENT_FILE, "award2.txt");
					mContext.startActivity(intent);
					break;
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.product_list_menu, menu);
		return true;
	}

	public class ListItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return itemTitle.length;
		}

		@Override
		public Object getItem(int pos) {
			return itemTitle[pos];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int pos, View v, ViewGroup vg) {
			View view = null;
			try {
				view = mContext.getLayoutInflater().inflate(
						R.layout.listview_item, vg, false);

				TextView title = (TextView) view
						.findViewById(R.id.text_summary);
				title.setText(itemTitle[pos]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}

	}
}
