package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.library.http.Alerts;
import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.Config;
import gov.jrj.library.http.JsonHttpResponseHandler;
import gov.jrj.ui.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MessageFragment extends ListFragment {

	private ListView listView;
	private JSONArray jsons = new JSONArray();
	private Activity mContext;
	private ProgressDialog pd;

	private void loadData() {
		SharedPreferences prefs =mContext.getSharedPreferences(Constants.KEY_SESSION_PREFS, 0);
		if(Config.isConnected(MessageFragment.this.getActivity())){
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Config.URL + "/message.php?uid="+prefs.getInt(Constants.KEY_UID, 0), new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject json) {
				Log.v("submit", json.toString());
				try {
					if (json.getInt("code") == 1) {
						jsons = json.getJSONArray("data");
						listView.setAdapter(new ListItemAdapter());
					}
				} catch (JSONException e) {
					Alerts.show(e.getMessage(), mContext);
				}
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
		});}
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
		try{
			JSONObject json = jsons.getJSONObject(position);
			Intent intent = new Intent();
			intent.setClass(mContext, TextNewsListActivity.class);
			intent.putExtra(Constants.KEY_SUBJECT_NAME, getString(R.string.tab2));
			intent.putExtra(Constants.KEY_ITEM_NAME, json.getString("title"));
			intent.putExtra(Constants.KEY_FILE, json.getString("file"));
			mContext.startActivity(intent);
		}catch(Exception ex){
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
							R.layout.message_item, vg, false);
				} else {
					view = v;
				}
				TextView title = (TextView)view.findViewById(R.id.textView1);
				TextView desc = (TextView)view.findViewById(R.id.textView2);
				JSONObject json = jsons.getJSONObject(pos);
				title.setText(json.getString("create_time"));
				desc.setText(json.getString("message"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
	}
}
