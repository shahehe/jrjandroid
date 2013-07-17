package gov.jrj.ui.util;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

public class Util {

	// session
	
	public static synchronized boolean isSessionValiad(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				Constants.KEY_SESSION_PREFS, 0);
		return prefs.getBoolean(Constants.KEY_IS_LOGINED, false);
	}

	public static String getCurrentUser(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				Constants.KEY_SESSION_PREFS, 0);
		return prefs.getString(Constants.KEY_USER_NAME, "");
	}

	public static interface ILoginListener {
		boolean onLoginSuccess(Context context,JSONObject data);
		void onLoginFail();
	}

	public static interface ILogoutListener {
		boolean onLogout(Context context);
	}
	
	public static void sendMessage(Handler handler, int code) {
		handler.sendEmptyMessage(code);
	}

}
