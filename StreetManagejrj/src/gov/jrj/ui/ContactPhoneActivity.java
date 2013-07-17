package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.ui.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ContactPhoneActivity extends Activity {


	String tel;
	TextView mTextTitle;
	ListView lstView; 

	String[] names; //= { "工委书记电话", "办公室主任电话", "武装部部长电话", "街道总工会主席电话" };
	String[] tels; //= { "66219688", "12345678", "12345678", "12345678" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.title_list);
		
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		lstView = (ListView) findViewById(R.id.list);
		
		Resources res = getResources();
		names = res.getStringArray(R.array.phone_names);
		tels = res.getStringArray(R.array.phone_nums);
		
		setTitle();
		setBackBtnLisenter();
		
		// add PhoneStateListener
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) this
			.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
 

		
		 ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();   
		        for(int i=0;i<names.length;i++)   
		         {   
		            HashMap<String, Object> temp = new HashMap<String, Object>();   
					temp.put("subject", names[i]);
					temp.put("ic_operate", tels[i]);
					temp.put("Image", R.drawable.ic_contact);
	 
					listItem.add(temp);   
		         }   
		         //生成适配器的Item和动态数组对应的元素   
		         SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源    
		             R.layout.contact_phone_item,//ListItem的XML实现   
		             //动态数组与ImageItem对应的子项           
		            new String[] {"subject","ic_operate", "Image"},    
		             //ImageItem的XML文件里面的一个ImageView,两个TextView ID   
		             new int[] {R.id.item_text,R.id.item_text_describe,R.id.call_out}   
		         );   
		           
		         //添加并且显示   
		         lstView.setAdapter(listItemAdapter);   
		            
		         //添加点击   
		          
		         lstView.setOnItemClickListener(new OnItemClickListener() {
		        	 
		             @Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,   
		                     long arg3) {   
		             	
						Intent callIntent = new Intent(Intent.ACTION_CALL);
						tel="tel:"+tels[arg2];
						callIntent.setData(Uri.parse(tel));
						startActivity(callIntent);
		 
		             }   
		        	 
				});
		              
	}
	
   



	
	
	private void setTitle() {
		String subTitle = "",subject="";
		subTitle = getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
		subject = getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
		mTextTitle.setText(subject + " > " + subTitle);

	}

	private void setBackBtnLisenter() {
		Button backBtn = (Button) findViewById(R.id.about_back_btn);
		backBtn.setOnClickListener(new OnClickListener() {

			 
			@Override
			public void onClick(View v) {

				ContactPhoneActivity.this.finish();
			}
		});
	} 



	
	//monitor phone call activities
	private class PhoneCallListener extends PhoneStateListener {
 
		private boolean isPhoneCalling = false;
 
		String LOG_TAG = "LOGGING 123";
 
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
 
			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// phone ringing
				Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
			}
 
			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active
				Log.i(LOG_TAG, "OFFHOOK");
 
				isPhoneCalling = true;
			}
 
			if (TelephonyManager.CALL_STATE_IDLE == state) {
				// run when class initial and phone call ended, 
				// need detect flag from CALL_STATE_OFFHOOK
				Log.i(LOG_TAG, "IDLE");
 
				if (isPhoneCalling) {
 
					Log.i(LOG_TAG, "restart app");
 
					// restart app
					
					ContactPhoneActivity.this.finish();

					isPhoneCalling = false;
					
				}
 
			}
		}
	}

	
	
}
