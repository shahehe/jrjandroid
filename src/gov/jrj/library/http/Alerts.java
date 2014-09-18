package gov.jrj.library.http;

import android.R;
import android.app.AlertDialog;
import android.content.Context;

public class Alerts {
	public static void show(CharSequence message, Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("提示");
		try {
			if (message.equals("")) {
				message = "网络错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络错误";
		}
		builder.setMessage(message);
		AlertDialog ad = builder.create();
		ad.show();
	}

	public static void show(Object message, Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("提示");
		if (((String) message).equals("")) {
			message = "网络错误";
		}
		try {
			if (message.equals("")) {
				message = "网络错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络错误";
		}
		builder.setMessage((String) message);
		AlertDialog ad = builder.create();
		ad.show();
	}

	public static void showOk(CharSequence title, CharSequence message) {
		// AlertDialog.Builder builder = new
		// AlertDialog.Builder(Session.getInstance().getMainActivity());
		// builder.setTitle(title);
		// builder.setMessage(message);
		// builder.setPositiveButton(R.string.ok,null);
		// AlertDialog ad = builder.create();
		// ad.show();
	}

	public static void showOk(CharSequence title, CharSequence message,
			Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.ok, null);
		AlertDialog ad = builder.create();
		ad.show();
	}

	public static void showError(CharSequence message, Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("test");
		builder.setMessage(message);
		builder.setPositiveButton(R.string.ok, null);
		AlertDialog ad = builder.create();
		ad.show();
	}

}
