package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.library.http.Alerts;
import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.Config;
import gov.jrj.library.http.JSONRPCHandler;
import gov.jrj.library.http.JSONRPCService;
import gov.jrj.library.http.JsonHttpResponseHandler;
import gov.jrj.library.http.Session;
import gov.jrj.ui.util.Constants;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomerListActivity extends Activity {

	private ProgressDialog pd;
	private Activity mContext;

	private List<Object> listData = new ArrayList<Object>();
	private List<String> listTag = new ArrayList<String>();

	ProgressBar progressBar;
	TextView mTextTitle;
	ListView lstView;
	Button btnBack;
	
	String addTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_list_activity);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		mTextTitle.setText(getIntent().getExtras().getString(
				Constants.KEY_SUBJECT_NAME));
		lstView = (ListView) findViewById(R.id.list);
		btnBack = (Button) findViewById(R.id.about_back_btn);
		this.progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		initViews();
		// this.setTitle();
		loadData();
	}

	@SuppressWarnings("unused")
	private void setTitle() {
		String subTitle = "", subject = "";
		subTitle = getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
		subject = getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
		mTextTitle.setText(subject + " > " + subTitle);

	}

	private void initViews() {
		lstView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					JSONObject json = (JSONObject) listData.get(arg2);
					addTime = json.getString("create_date");
					AsyncHttpClient client = new AsyncHttpClient();
					int uid = getSharedPreferences(Constants.KEY_SESSION_PREFS, 0)
							.getInt(Constants.KEY_UID, 0);
					client.get(Config.URL + "/creditLog.php?uid=" + uid + "&type=customer&extra=" + URLEncoder.encode(addTime),
							new JsonHttpResponseHandler() {
							});
					
					Intent intent = new Intent(CustomerListActivity.this,
							DetailActivity.class);
					// intent.putExtra("data", json.toString());
					intent.putExtra(Constants.KEY_TITLE_BAR, "消费提醒");
					intent.putExtra(Constants.KEY_SUBJECT_NAME,
							json.getString("title"));
					intent.putExtra(Constants.KEY_ITEM_NAME,
							json.getString("content"));
					intent.putExtra("date", json.getString("department"));
					if (!json.getString("pic").equals("")) {
						intent.putExtra("image", json.getString("pic"));
					}
					startActivity(intent);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CustomerListActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.product_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			this.listData.clear();
			this.listTag.clear();
			this.getData("Commerce.getList");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void loadData() {
		String url = "Commerce.getList";
		String data = Config.getCacheData(this, url);
		if (data == null) {
			Session.getInstance().setCheckedProduceUpdate(false);
			this.getData(url);
		} else {
			try {
				this.processData(new JSONObject(data));
				Session.getInstance().setCheckedProduceUpdate(true);
				this.getData(url);
			} catch (Exception ex) {
				this.getData(url);
			}
		}
	}

	private void getData(final String url) {
		if (Session.getInstance().isProductUpdate()) {
			this.progressBar.setVisibility(View.VISIBLE);
		} else {
			this.progressBar.setVisibility(View.INVISIBLE);
		}
		JSONRPCService.exec(url, null, new JSONRPCHandler() {

			@Override
			public void onSuccess(JSONObject json) {
				Config.setCacheData(CustomerListActivity.this, url,
						json.toString());
				System.out.println(json);
				processData(json);
			}

			@Override
			public void onFailed(String message) {
				pd.dismiss();
			}

			@Override
			public void onError(String message) {
				if (pd != null)
					pd.dismiss();
				Alerts.show(message, CustomerListActivity.this);
			}

			@Override
			public void onStart() {
				if (!Session.getInstance().isProductUpdate()) {
					pd = ProgressDialog.show(CustomerListActivity.this, null,
							getString(R.string.loading));
				}
			}

			@Override
			public void onFinish() {
				if (pd != null) {
					pd.dismiss();
				}

			}

		});
	}

	private void processData(JSONObject json) {
		try {
			if (Session.getInstance().isProductUpdate()) {
				listTag.clear();
				listData.clear();
				Session.getInstance().setCheckedProduceUpdate(false);
				this.progressBar.setVisibility(View.INVISIBLE);
			}
			JSONArray commerce = json.getJSONArray("commerce");
			for (int i = 0; i < commerce.length(); i++) {
				JSONObject p = commerce.getJSONObject(i);
				listTag.add(p.getString("moth"));
				listData.add(p.getString("moth"));
				if (p.get("items").getClass().equals(JSONArray.class)) {
					JSONArray items = p.getJSONArray("items");
					for (int x = 0; x < items.length(); x++) {
						JSONObject item = items.getJSONObject(x);
						listData.add(item);
					}
				} else {
					listData.add(p.get("item"));
				}
			}
			lstView.setAdapter(new ListItemAdapter());
		} catch (JSONException e) {
			e.printStackTrace();
			// Alerts.show(e.getMessage(),TextNewsListActivity.this);

		}
	}

	public class ListItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public Object getItem(int pos) {
			return listData.get(pos);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public boolean isEnabled(int position) {
			if (listTag.contains(getItem(position))) {
				return false;
			}
			return super.isEnabled(position);
		}

		@Override
		public View getView(int pos, View v, ViewGroup vg) {
			View view = null;
			try {
				if (listTag.contains(getItem(pos))) {
					view = mContext.getLayoutInflater().inflate(
							R.layout.product_tag_item, vg, false);
					view.setBackgroundResource(R.drawable.product_moth_bg);
					TextView title = (TextView) view
							.findViewById(R.id.textView1);
					title.setTextColor(Color.WHITE);
					title.setText(getItem(pos).toString());
				} else {
					view = mContext.getLayoutInflater().inflate(
							R.layout.product_list_item, vg, false);
					TextView title = (TextView) view
							.findViewById(R.id.textView1);
					TextView desc = (TextView) view
							.findViewById(R.id.textView2);
					JSONObject json = (JSONObject) getItem(pos);
					title.setText(json.getString("number") + ": "
							+ json.getString("title"));
					String descStr = json.getString("content");
					desc.setText(descStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}

	}
}
