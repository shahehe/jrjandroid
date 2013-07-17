package gov.jrj.Boardcast;

import gov.jrj.ui.PushService;
import gov.jrj.ui.util.Constants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootBoardcast extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isAutoPush = context.getSharedPreferences(PushService.TAG,
				android.content.Context.MODE_PRIVATE).getBoolean("isAutoPush",
				false);
		SharedPreferences prefs = context.getSharedPreferences(
				Constants.KEY_SESSION_PREFS, 0);
		int uid = prefs.getInt(Constants.KEY_UID, 0);
		if (isAutoPush && uid != 0) {
			Intent service = new Intent(context, PushService.class);
			service.setAction(PushService.ACTION_START);
			context.startService(service);
			Log.v("TAG", "PushService Started");
		}

	}
}
