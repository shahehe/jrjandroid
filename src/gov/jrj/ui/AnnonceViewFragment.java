package gov.jrj.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gov.jrj.R;
import gov.jrj.library.http.Alerts;
import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.Config;
import gov.jrj.library.http.ImageLoader;
import gov.jrj.library.http.JsonHttpResponseHandler;
import gov.jrj.library.http.Session;
import gov.jrj.ui.util.Constants;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AnnonceViewFragment extends ListFragment {

	private ListView listView;
	private JSONArray jsons = new JSONArray();
	private Activity mContext;
	private ProgressDialog pd;

	private void loadData() {
		
		String url = Config.URL + "/index.php?file=list.xml";
		// 读取缓存
		String data = Config.getCacheData(this.getActivity(), url);
		if (data == null) {
			// 读取本地
			data = Config.readRawData(this.getActivity(), R.raw.list);
			if (data == null) {
				this.getData(url);
			} else {
				try {
					this.processData(new JSONObject(data));
					if (!Session.getInstance().isCheckedUpdate()) {
						this.checkUpdate(url, data);
					}
				} catch (Exception ex) {
					this.getData(url);
				}
			}
		} else {
			try {
				this.processData(new JSONObject(data));
				if (!Session.getInstance().isCheckedUpdate()) {
					this.checkUpdate(url, data);
				}
			} catch (Exception ex) {
				this.getData(url);
			}

		}
	}

	public void getData(final String url) {
		if (Config.isConnected(this.getActivity())) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(url, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					Log.v("submit", json.toString());
					Config.setCacheData(AnnonceViewFragment.this.getActivity(),
							url, json.toString());
					processData(json);
				}

				@Override
				public void onStart() {
					pd = ProgressDialog.show(mContext, null,
							getString(R.string.loading));
				}

				@Override
				public void onFinish() {
					pd.dismiss();
				}

				@Override
				public void onFailure(Throwable e) {
					Alerts.show(e.getMessage(), mContext);
				}
			});
		} else {
			Toast.makeText(mContext, "网络无法访问", Toast.LENGTH_SHORT).show();
		}

	}

	public void checkUpdate(final String url, final String data) {
		if (Config.isConnected(this.getActivity())) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(url, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					String md5 = Config.getMD5(json.toString());
					String dataMd5 = Config.getMD5(data);
					if (md5.equals(dataMd5)) {

						Toast.makeText(mContext, "周报列表已经是最新",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(mContext, "周报列表更新", Toast.LENGTH_SHORT)
								.show();
						getData(url);
						processData(json);
					}
					Session.getInstance().setCheckedUpdate(true);
				}
			});
		}

	}

	public void processData(JSONObject json) {
		try {
			if (json.getInt("code") == 1) {
				JSONObject items = json.getJSONObject("data").getJSONObject(
						"items");
				if (items.get("item").getClass().equals(JSONArray.class)) {
					jsons = items.getJSONArray("item");
				} else {
					jsons.put(items.getJSONObject("item"));
				}
				listView.setAdapter(new ListItemAdapter());
			}
		} catch (JSONException e) {
			Alerts.show(e.getMessage(), mContext);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = this.getActivity();
		initViews();
		setListAdapter(new ListItemAdapter());
		loadData();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		try {
			JSONObject json = jsons.getJSONObject(position);
			Intent intent = new Intent();
			intent.setClass(mContext, TextNewsListActivity.class);
			intent.putExtra(Constants.KEY_SUBJECT_NAME,
					getString(R.string.tab2));
			intent.putExtra(Constants.KEY_ITEM_NAME, json.getString("title"));
			intent.putExtra(Constants.KEY_FILE, json.getString("file"));
			mContext.startActivity(intent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void initViews() {
		listView = getListView();
		listView.setCacheColorHint(Color.WHITE);
		listView.setDivider(this.getActivity().getResources()
				.getDrawable(R.drawable.app_divider_h_gray));
	}

	public class ListItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return jsons.length();
		}

		@Override
		public Object getItem(int pos) {
			try {
				return jsons.get(pos);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int pos, View v, ViewGroup vg) {
			View view = null;
			try {
				if (v == null) {
					view = mContext.getLayoutInflater().inflate(
							R.layout.daily_list_item, vg, false);
				} else {
					view = v;
				}
				TextView title = (TextView) view.findViewById(R.id.textView1);
				TextView desc = (TextView) view.findViewById(R.id.textView2);
				ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
				JSONObject json = jsons.getJSONObject(pos);
				if (json.has("image")) {
					String url = json.getString("image");
					ImageLoader.loadImage(
							AnnonceViewFragment.this.getActivity(), iv, url);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
	}
}