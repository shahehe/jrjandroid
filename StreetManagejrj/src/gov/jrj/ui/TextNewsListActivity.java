package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.library.http.Alerts;
import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.Config;
import gov.jrj.library.http.ImageLoader;
import gov.jrj.library.http.JsonHttpResponseHandler;
import gov.jrj.ui.util.Constants;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TextNewsListActivity extends Activity {

	private List<Object> listData = new ArrayList<Object>();
	private List<String> listTag = new ArrayList<String>();

	private ProgressDialog pd;
	private Activity mContext;
	private String mFile;

	TextView mTextTitle;
	ListView lstView;
	Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.title_list);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		lstView = (ListView) findViewById(R.id.list);
		btnBack = (Button) findViewById(R.id.about_back_btn);
		mFile = getIntent().getExtras().getString(Constants.KEY_FILE);
		initViews();
		setTitle();
		loadData();
	}

	private void setTitle() {
		String subTitle = "", subject = "";
		subTitle = getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
		subject = getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
		mTextTitle.setText(subTitle);

	}

	public static final String PREFS_NAME = "data";

	public void setAdapter(JSONArray sections) {
		try {
			for (int i = 0; i < sections.length(); i++) {
				JSONObject sec = sections.getJSONObject(i);
				listTag.add(sec.getString("title"));
				listData.add(sec.getString("title"));
				if (sec.get("item").getClass().equals(JSONArray.class)) {
					JSONArray items = sec.getJSONArray("item");
					for (int x = 0; x < items.length(); x++) {
						JSONObject item = items.getJSONObject(x);
						listData.add(item);
					}
				} else {
					listData.add(sec.get("item"));
				}
			}
			lstView.setAdapter(new ListItemAdapter());
		} catch (Exception ex) {

		}
	}

	private void loadData() {
		try {
			// 取得本地数据
			final SharedPreferences settings = getSharedPreferences(PREFS_NAME,
					0);
			if (settings.contains(mFile)) {
				String data = settings.getString(mFile, null);
				JSONArray sections = new JSONArray(data);
				setAdapter(sections);
			} else {
				loadWebData();
			}
		} catch (Exception ex) {
			loadWebData();
		}
	}

	private void loadWebData() {
		if (Config.isConnected(this)) {
			int uid = getSharedPreferences(Constants.KEY_SESSION_PREFS, 0)
					.getInt(Constants.KEY_UID, 0);
			AsyncHttpClient credit = new AsyncHttpClient();
			String[] filename = mFile.split("\\.");
			credit.get(Config.URL + "/newspaper.php?uid=" + uid + "&file="
					+ filename[0], new JsonHttpResponseHandler() {
			});

			AsyncHttpClient client = new AsyncHttpClient();
			//mFile = "126.xml";
			client.get(Config.URL + "/index.php?file=" + mFile,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject json) {
							Log.v("submit", json.toString());
							try {
								// Mark Bug
								if (json.getInt("code") == 1) {
									JSONObject jsonObj = json.getJSONObject(
											"data").getJSONObject("items");
									JSONArray sections = jsonObj
											.optJSONArray("section");
									// jsonObj.toJSONArray(sections);
									if (sections != null) {
										setAdapter(sections);
										// 保存到李地
										SharedPreferences settings = getSharedPreferences(
												PREFS_NAME, 0);
										SharedPreferences.Editor editor = settings
												.edit();
										editor.putString(mFile,
												sections.toString());
										editor.commit();
									}else{
										JSONArray temp = new JSONArray();
										temp.put(jsonObj.getJSONObject("section"));
										setAdapter(temp);
										// 保存到李地
										SharedPreferences settings = getSharedPreferences(
												PREFS_NAME, 0);
										SharedPreferences.Editor editor = settings
												.edit();
										editor.putString(mFile,
												temp.toString());
										editor.commit();
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
								Alerts.show(e.getMessage(),
										TextNewsListActivity.this);

							}

						}

						@Override
						public void onStart() {
							pd = ProgressDialog.show(TextNewsListActivity.this,
									null, getString(R.string.loading));
						}

						@Override
						public void onFinish() {
							pd.dismiss();
						}

						@Override
						public void onFailure(Throwable e) {
							Alerts.show(e.getMessage(),
									TextNewsListActivity.this);
						}

					});
		}
	}

	private void initViews() {
		lstView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					JSONObject json = (JSONObject) listData.get(arg2);
					Intent intent = new Intent(TextNewsListActivity.this,
							DetailActivity.class);
					intent.putExtra(Constants.KEY_TITLE_BAR, mTextTitle
							.getText().toString());
					intent.putExtra(Constants.KEY_SUBJECT_NAME,
							json.getString("title"));
					intent.putExtra(Constants.KEY_ITEM_NAME,
							json.getString("content"));
					intent.putExtra("date", json.getString("date"));
					if (json.has("image")) {
						intent.putExtra("image", json.getString("image"));
					}
					startActivity(intent);
				} catch (Exception ex) {

				}
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TextNewsListActivity.this.finish();
			}
		});
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
							R.layout.daily_tag_item, vg, false);
					view.setBackgroundResource(R.drawable.product_moth_bg);
					TextView title = (TextView) view
							.findViewById(R.id.textView1);
					title.setTextColor(Color.WHITE);
					title.setText(getItem(pos).toString());
				} else {
					view = mContext.getLayoutInflater().inflate(
							R.layout.daily_section_item, vg, false);
					TextView title = (TextView) view
							.findViewById(R.id.textView1);
					TextView desc = (TextView) view
							.findViewById(R.id.textView2);
					ImageView iv = (ImageView) view
							.findViewById(R.id.imageView1);
					JSONObject json = (JSONObject) getItem(pos);
					if (json.has("image")) {
						String url = json.getString("image");
						ImageLoader.loadImage(TextNewsListActivity.this, iv,
								url);
						iv.setVisibility(View.VISIBLE);
						desc.setMaxEms(15);
						title.setMaxEms(12);
					} else {
						iv.setVisibility(View.INVISIBLE);
						desc.setMaxEms(20);
						title.setMaxEms(16);
					}
					title.setText(json.getString("title"));
					desc.setText(json.getString("desc"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}

	}
}
