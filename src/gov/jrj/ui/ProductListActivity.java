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

public class ProductListActivity extends Activity {
	
	private ProgressDialog pd;
	private Activity mContext;
	
	private List<Object> listData = new ArrayList<Object>();
	private List<String> listTag = new ArrayList<String>();
	
	ProgressBar progressBar;
	TextView mTextTitle;
	ListView lstView;
	Button btnBack;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_list_activity);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		mTextTitle.setText("下架商品列表");
		lstView = (ListView) findViewById(R.id.list);
		btnBack = (Button) findViewById(R.id.about_back_btn);
		this.progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		initViews();
		//this.setTitle();
		loadData();
	}
	
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
				try{
					JSONObject json = (JSONObject)listData.get(arg2);
					Intent intent = new Intent(ProductListActivity.this,ProductInfoActivity.class);
					intent.putExtra("data", json.toString());
					startActivity(intent);
				}catch(Exception ex){
					
				}
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ProductListActivity.this.finish();
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
		switch(item.getItemId()){
		case R.id.item1:
			this.listData.clear();
			this.listTag.clear();
			this.getData("Product.getList");
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void loadData() {
		String url = "Product.getList";
		String data = Config.getCacheData(this, url);
		if(data == null){
			Session.getInstance().setCheckedProduceUpdate(false);
			this.getData(url);
		}else{
			try{
				this.processData(new JSONObject(data));
				Session.getInstance().setCheckedProduceUpdate(true);
				this.getData(url);
			}catch(Exception ex){
				this.getData(url);
			}
		}
	}

	private void getData(final String url){
		if(Session.getInstance().isProductUpdate()){
			this.progressBar.setVisibility(View.VISIBLE);
		}else{
			this.progressBar.setVisibility(View.INVISIBLE);
		}
		JSONRPCService.exec(url, null, new JSONRPCHandler(){

			@Override
			public void onSuccess(JSONObject json) {
				Config.setCacheData(ProductListActivity.this, url, json.toString());
				System.out.println(json);
				processData(json);
			}

			@Override
			public void onFailed(String message) {
				pd.dismiss();
			}

			@Override
			public void onError(String message) {
				if(pd != null) pd.dismiss();
				Alerts.show(message,ProductListActivity.this);
			}

			@Override
			public void onStart() {
				if(!Session.getInstance().isProductUpdate()){
					pd = ProgressDialog.show(ProductListActivity.this, null, getString(R.string.loading));
				}
			}

			@Override
			public void onFinish() {
				if(pd != null){
					pd.dismiss();
				}
				
			}
			
		});
	}
	
	private void processData(JSONObject json){
		try {
			if(Session.getInstance().isProductUpdate()){
				listTag.clear();
				listData.clear();
				Session.getInstance().setCheckedProduceUpdate(false);
				this.progressBar.setVisibility(View.INVISIBLE);
			}
			JSONArray products = json.getJSONArray("products");
			for(int i=0;i<products.length();i++){
				JSONObject p = products.getJSONObject(i);
				listTag.add(p.getString("moth"));
				listData.add(p.getString("moth"));
				if(p.get("items").getClass().equals(JSONArray.class)){
					JSONArray items = p.getJSONArray("items");
					for(int x=0;x<items.length();x++){
						JSONObject item = items.getJSONObject(x);
						listData.add(item);
					}
				}else{
					listData.add(p.get("item"));
				}
			}
			lstView.setAdapter(new ListItemAdapter());
		} catch (JSONException e) {
			e.printStackTrace();
			//Alerts.show(e.getMessage(),TextNewsListActivity.this);
			
		}
	}
	
	public class ListItemAdapter extends BaseAdapter{
		
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
			if(listTag.contains(getItem(position))){
				return false;
			}
			return super.isEnabled(position);
		}

		@Override
		public View getView(int pos, View v, ViewGroup vg) {
			View view = null;
			try {
				if(listTag.contains(getItem(pos))){
					view = mContext.getLayoutInflater().inflate(R.layout.product_tag_item, vg,false);
					view.setBackgroundResource(R.drawable.product_moth_bg);
					TextView title = (TextView)view.findViewById(R.id.textView1);
					title.setTextColor(Color.WHITE);
					title.setText(getItem(pos).toString());
				}else{
					view = mContext.getLayoutInflater().inflate(R.layout.product_list_item, vg,false);
					TextView title = (TextView)view.findViewById(R.id.textView1);
					TextView desc = (TextView)view.findViewById(R.id.textView2);
					JSONObject json = (JSONObject)getItem(pos);
					title.setText(json.getString("number") +": "+ json.getString("name") );
					String descStr = "商标："+json.getString("brand")+"；"+json.getString("company")+"；规格型号："+json.getString("model");
					desc.setText(descStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
		
	}
}
