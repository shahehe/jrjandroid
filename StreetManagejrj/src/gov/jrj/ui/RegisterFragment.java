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
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class RegisterFragment extends Fragment {

	Button loginBtn;
	Button registerBtn;
	EditText userEdit;
	EditText pwdEdit;
	Context mContext;
	String userName;
	String password;
	Handler mHandler;
//	EditText emailEdit;

	private ProgressDialog pd;
	
	public RegisterFragment(Handler handler) {
		mHandler = handler;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}
		
		mContext = getActivity(); 
		View view = inflater.inflate(R.layout.register_fragment,
				container, false);
		
	       Button registerBtn = (Button)view.findViewById(R.id.buttonRegister);
	       registerBtn.setOnClickListener(new RegisterOnClickListener());
	       userEdit = (EditText)view.findViewById(R.id.edit_text1);
	       pwdEdit = (EditText)view.findViewById(R.id.edit_text2);
//	       emailEdit = (EditText)view.findViewById(R.id.edit_text3);
//	       emailEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		return view;
	}
	class RegisterOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
				if(TextUtils.isEmpty(userEdit.getText())){
					Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
					return;
//				}else if(TextUtils.isEmpty(emailEdit.getText())){
//					Toast.makeText(mContext, "电子邮件不能为空", Toast.LENGTH_SHORT).show();
//					return;
				}else if(TextUtils.isEmpty(pwdEdit.getText()) ){
					Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}else{
					
					SharedPreferences mPrefs = mContext.getSharedPreferences(PushService.TAG, Context.MODE_PRIVATE);
					String deviceID = mPrefs.getString(PushService.PREF_DEVICE_ID, null);
					RequestParams params = new RequestParams();
					params.put("name", userEdit.getText().toString());
//					params.put("email", emailEdit.getText().toString());
					params.put("password", pwdEdit.getText().toString());
					params.put("deviceid", deviceID);
					if(Config.isConnected(RegisterFragment.this.getActivity())){
					AsyncHttpClient client = new AsyncHttpClient();
					client.post(Config.URL+"/register.php",params, new JsonHttpResponseHandler(){
						@Override
						public void onSuccess(JSONObject response) {
							try{
								
								if(response.getInt("code") == 1){
			
									
									Toast.makeText(RegisterFragment.this.getActivity(), "注册成功！感谢您的注册，我们会妥善保管您的个人信息，并会在有需要的时候和您取得联系。", Toast.LENGTH_SHORT).show();
									
									SharedPreferences prefs =mContext.getSharedPreferences(Constants.KEY_SESSION_PREFS, 0);
									Editor editor = prefs.edit();
									editor.putString(Constants.KEY_USER_NAME, userEdit.getText().toString());
									editor.putBoolean(Constants.KEY_IS_LOGINED, true);
									editor.putInt(Constants.KEY_UID, response.getInt("uid"));
									editor.commit();
									SharedPreferences autoPrefs = mContext.getSharedPreferences(
											PushService.TAG,
											android.content.Context.MODE_PRIVATE);
									Editor editorAuto = autoPrefs.edit();
									editorAuto.putBoolean("isAutoPush", true);
									editorAuto.commit();
									Util.sendMessage(Session.getInstance().getHandler(), Constants.WHAT_LOGIN);
								}else{
									Toast.makeText(RegisterFragment.this.getActivity(), "return code error", Toast.LENGTH_SHORT).show();
									Alerts.show(response.getString("message"), RegisterFragment.this.getActivity());
								}
							}catch(Exception ex){
								Toast.makeText(RegisterFragment.this.getActivity(), "return code error 111", Toast.LENGTH_SHORT).show();
								ex.printStackTrace();
							}
							
							System.out.println(response.toString());
							pd.dismiss();
						}

						@Override
						public void onStart() {
							pd = ProgressDialog.show(RegisterFragment.this.getActivity(), null,"正在注册中...");
						}

						@Override
						public void onFinish() {
							//Toast.makeText(RegisterFragment.this.getActivity(), "return code error 222", Toast.LENGTH_SHORT).show();
							pd.dismiss();
						}

						@Override
						public void onFailure(Throwable error) {
							//Toast.makeText(RegisterFragment.this.getActivity(), "return code error 333", Toast.LENGTH_SHORT).show();
							Alerts.show(error.getMessage(), RegisterFragment.this.getActivity());
							pd.dismiss();
						}
						
					}); }
				}
			
			
		}
	}
	public RegisterFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

}
