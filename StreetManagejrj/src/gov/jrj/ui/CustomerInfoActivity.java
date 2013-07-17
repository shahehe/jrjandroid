package gov.jrj.ui;

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
import gov.jrj.ui.ProductListActivity.ListItemAdapter;
import gov.jrj.ui.util.Constants;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CustomerInfoActivity extends Activity {

	private JSONArray jsons = new JSONArray();
	private Activity mContext;
	private ProgressDialog pd;

	ProgressBar progressBar;
	TextView mTextTitle;
	Button btnBack;
	JSONObject data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.health_activity);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		mTextTitle.setText("健康服务");
		btnBack = (Button) findViewById(R.id.about_back_btn);
		this.progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		initViews();
	}


	private void initViews() {


		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CustomerInfoActivity.this.finish();
			}
		});
		try {
			data = new JSONObject(this.getIntent().getStringExtra("data"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}