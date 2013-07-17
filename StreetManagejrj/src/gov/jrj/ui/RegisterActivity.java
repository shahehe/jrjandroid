package gov.jrj.ui;

import org.json.JSONObject;


import gov.jrj.R;
import gov.jrj.library.http.Alerts;
import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.Config;
import gov.jrj.library.http.JsonHttpResponseHandler;
import gov.jrj.library.http.RequestParams;
import gov.jrj.library.http.Session;
import gov.jrj.ui.util.Constants;
import gov.jrj.ui.util.Util;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener{

	
	private ProgressDialog pd;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        Button btn = (Button)this.findViewById(R.id.button1);
        btn.setOnClickListener(this);
        EditText mailET = (EditText)this.findViewById(R.id.editText2);
        //mailET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_register, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button1){
			final EditText nameET = (EditText)this.findViewById(R.id.editText1);
			EditText mailET = (EditText)this.findViewById(R.id.editText2);
			final EditText passwordET = (EditText)this.findViewById(R.id.editText3);
			if(TextUtils.isEmpty(nameET.getText())){
				Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
				return;
//			}else if(TextUtils.isEmpty(mailET.getText())){
//				Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
//				return;
			}else if(TextUtils.isEmpty(passwordET.getText())){
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}else{
				SharedPreferences mPrefs = getSharedPreferences(PushService.TAG, Context.MODE_PRIVATE);
				String deviceID = mPrefs.getString(PushService.PREF_DEVICE_ID, null);
				RequestParams params = new RequestParams();
				params.put("name", nameET.getText().toString());
				params.put("email", mailET.getText().toString());
				params.put("password", passwordET.getText().toString());
				params.put("deviceid", deviceID);
				if(Config.isConnected(RegisterActivity.this)){
				AsyncHttpClient client = new AsyncHttpClient();
				client.post(Config.URL+"/reg.php",params, new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(JSONObject response) {
						try{
							if(response.getInt("code") == 1){
								//Intent i = new Intent();
								//i.setClass(RegisterActivity.this, ResultActivity.class);
								//i.putExtra("message", response.getString("message"));
								//RegisterActivity.this.startActivity(i);
								RegisterActivity.this.finish();
								//PushService.actionStart(getApplicationContext());
								//login(nameET.getText().toString(), passwordET.getText().toString());
								
								Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
								
								SharedPreferences prefs =getSharedPreferences(Constants.KEY_SESSION_PREFS, 0);
								Editor editor = prefs.edit();
								editor.putString(Constants.KEY_USER_NAME, nameET.getText().toString());
								editor.putBoolean(Constants.KEY_IS_LOGINED, true);
								editor.putInt(Constants.KEY_UID, response.getInt("uid"));
								editor.commit();
								
								Util.sendMessage(Session.getInstance().getHandler(), Constants.WHAT_LOGIN);
							}else{
								Alerts.show(response.getString("message"), RegisterActivity.this);
							}
						}catch(Exception ex){
							ex.printStackTrace();
						}
						
						System.out.println(response.toString());
					}

					@Override
					public void onStart() {
						pd = ProgressDialog.show(RegisterActivity.this, null,"正在注册中...");
					}

					@Override
					public void onFinish() {
						pd.dismiss();
					}

					@Override
					public void onFailure(Throwable error) {
						Alerts.show(error.getMessage(), RegisterActivity.this);
					}
					
				});}
			}
		}
	}

	public RegisterActivity() {
		super();
		// TODO Auto-generated constructor stub
	}
}
