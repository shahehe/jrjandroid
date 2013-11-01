package gov.jrj.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import gov.jrj.R;
import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.AsyncHttpResponseHandler;
import gov.jrj.library.http.Config;
import gov.jrj.library.http.ImageLoader;
import gov.jrj.library.http.JsonHttpResponseHandler;
import gov.jrj.library.http.RequestParams;
import gov.jrj.library.http.Session;
import gov.jrj.library.http.StartImageLoader;
import gov.jrj.ui.app.StartupDialog;
import gov.jrj.ui.util.Constants;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	StartupDialog dialog;
	boolean nc = true;

	private String getVersionName() throws Exception {

		PackageManager packageManager = getPackageManager();

		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}

	protected void showUpdataDialog(final String downloadUrl) {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("版本升级");
		builer.setMessage("更多惊喜");
		// 当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				downLoadApk(downloadUrl);
			}
		});
		// 当点取消按钮时进行登录
		builer.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// LoginMain();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	public static File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		// �����ȵĻ���ʾ��ǰ��sdcard�������ֻ��ϲ����ǿ��õ�
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			// ��ȡ���ļ��Ĵ�С
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(),
					"updata.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// ��ȡ��ǰ������
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			return null;
		}
	}

	protected void downLoadApk(final String downloadUrl) {
		final ProgressDialog pd; // ������Ի���
		pd = new ProgressDialog(MainActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Message msg = new Message();
		} else {
			pd.show();
			new Thread() {
				@Override
				public void run() {
					try {
						File file = getFileFromServer(downloadUrl, pd);
						sleep(1000);
						installApk(file);
						pd.dismiss(); // �����������Ի���

					} catch (Exception e) {
						Message msg = new Message();
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	// ��װapk
	protected void installApk(File file) {
		Intent intent = new Intent();
		// ִ�ж���
		intent.setAction(Intent.ACTION_VIEW);
		// ִ�е��������
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabFragment tf = new TabFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		// LinearLayout linner = (LinearLayout)
		// findViewById(R.id.pf_main_content_fragment);
		// Fragment fragment ;
		// transaction.remove(fragment);
		transaction.replace(R.id.pf_main_bottom_fragment_layout, tf);
		transaction.addToBackStack(null);
		transaction.commit();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.main_fragment);
		// Push DeviceID
		String mDeviceID = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		Editor editor = getSharedPreferences(PushService.TAG, MODE_PRIVATE)
				.edit();
		editor.putString(PushService.PREF_DEVICE_ID, mDeviceID);
		editor.commit();
		nc = getIntent().getBooleanExtra("nc", true);
		boolean isAutoPush = getSharedPreferences(PushService.TAG, MODE_PRIVATE)
				.getBoolean("isAutoPush", false);
		int uid = getSharedPreferences(Constants.KEY_SESSION_PREFS, MODE_PRIVATE).getInt(
				Constants.KEY_UID, 0);
		if (isAutoPush && uid != 0) {
			boolean isStarted = getSharedPreferences(PushService.TAG,
					android.content.Context.MODE_PRIVATE).getBoolean(
					"isStarted", false);
			if (!isStarted) {
				if (!PushService.isServiceRunning(this)) {
					PushService.actionStart(getApplicationContext());
					System.out.print("start");
				}
			}
		} else {
			try {
				Log.d("push", "stop service");
				PushService.actionStop(getApplicationContext());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.print("stop");
		}
		if (uid == 0 && !mDeviceID.equals("")) {
			if (!PushService.isServiceRunning(this)) {
				PushService.actionStart(getApplicationContext());
				System.out.print("start");
			}
		}

		// 检查服务器是否正常
		/*
		 * if(Config.isConnected(this)){ Toast.makeText(MainActivity.this, "h1",
		 * Toast.LENGTH_SHORT).show(); if
		 * (!Config.isHttpAvail("http://64.150.161.193") ) { if (
		 * !Config.isHttpAvail("http://64.150.161.193") ) { Config.httpOK =
		 * false; Toast.makeText(MainActivity.this, "h3",
		 * Toast.LENGTH_SHORT).show(); } else {
		 * Toast.makeText(MainActivity.this, "h4", Toast.LENGTH_SHORT).show();
		 * Config.URL = Config.URLBackup; } } else
		 * Toast.makeText(MainActivity.this, "h2", Toast.LENGTH_SHORT).show(); }
		 * else { Config.httpOK = false; Toast.makeText(MainActivity.this, "h5",
		 * Toast.LENGTH_SHORT).show(); }
		 */

		if (Config.isConnected(this) && nc) {
			AsyncHttpClient clientDevice = new AsyncHttpClient();
			RequestParams deviceIDPara = new RequestParams();
			deviceIDPara.put("deviceid", mDeviceID);
			clientDevice.post(Config.URL + "/device.php", deviceIDPara,
					new JsonHttpResponseHandler() {
					});
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(Config.URL + "/serverstatus.php",
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject response) {
							try {

								if (response.getInt("status") == 1) {
									// PushService.actionStart(getApplicationContext());
									// Toast.makeText(MainActivity.this,
									// "消息推送服务器正常", Toast.LENGTH_SHORT).show();
								} else {
									// Toast.makeText(MainActivity.this,
									// "消息推送服务器无法访问",
									// Toast.LENGTH_SHORT).show();
								}
								if (response.getString("version")
										.equalsIgnoreCase(getVersionName())) {
									// Toast.makeText(MainActivity.this,
									// "no update needed " +
									// response.getString("url") + "\t" +
									// response.getString("version") ,
									// Toast.LENGTH_SHORT).show();
								} else {
									// Toast.makeText(MainActivity.this,
									// "update needed " +
									// response.getString("url") + "\t" +
									// response.getString("version") ,
									// Toast.LENGTH_SHORT).show();
									showUpdataDialog(response.getString("url"));

								}
							} catch (Exception ex) {
								// Toast.makeText(MainActivity.this,
								// "消息推送服务器无法访问", Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(Throwable error) {
							Toast.makeText(MainActivity.this, "消息推送服务器无法访问",
									Toast.LENGTH_SHORT).show();
						}
					});
			AsyncHttpClient startMessage = new AsyncHttpClient();
			startMessage.get(Config.URL + "/startupmessage.php",
					new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(JSONObject response) {
							// TODO Auto-generated method stub
							super.onSuccess(response);
							try {
								int id = response.getInt("id");
								SharedPreferences sp = getSharedPreferences(
										"startupImage", MODE_PRIVATE);
								int currId = sp.getInt("id", -1);
								boolean isShown = sp
										.getBoolean("isShown", true);

								if (currId < id) {
									startupMessage(response);
									Config.clearCacheData(MainActivity.this,
											response.getString("html"));
									Editor editor = sp.edit();
									editor.putBoolean("isShown", true);
									editor.putInt("id", response.getInt("id"));
									editor.commit();
								} else if (isShown) {
									startupMessage(response);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}

	}

	private void startupMessage(final JSONObject response) {
		try {
			StartupDialog.Builder customBuilder = new StartupDialog.Builder(
					MainActivity.this);

			customBuilder.setTitle("温馨提示").setMessage(
					response.getString("message"));
			String html = response.getString("html");
			if (!html.equals("")) {
				customBuilder.setPositiveButton("查看详情",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								try {
									String html = response.getString("html");
									//debug
									//html = "http://218.249.192.55/jrj/startuphtml/20130916.htm";
									String title = response.getString("title");
									if (title == null || title.equals("")) {
										title = getString(R.string.startupmessage);
									}
									String data = Config.getCacheData(
											MainActivity.this, html);

									if (data == null
											|| data.equals("")
											|| !Config.getMD5(data).equals(
													response.getString("md5"))) {
										// String md5 = Config.getMD5(data);
										System.out.println("No cache");
										Bundle bundle = new Bundle();
										Intent intent = new Intent();
										bundle.putString("cache", "N");
										bundle.putString("url", html);
										bundle.putString("title", title);
										intent.putExtras(bundle);
										intent.setClass(MainActivity.this,
												StartupWebViewActivity.class);
										startActivity(intent);
									} else {
										Bundle bundle = new Bundle();
										Intent intent = new Intent();
										bundle.putString("cache", "Y");
										bundle.putString("url", html);
										bundle.putString("title", title);
										intent.putExtras(bundle);
										intent.setClass(MainActivity.this,
												StartupWebViewActivity.class);
										startActivity(intent);
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								dialog.dismiss();
							}

						});
			}

			customBuilder.setNoMoreButton("不再显示",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SharedPreferences sp = getSharedPreferences(
									"startupImage", MODE_PRIVATE);
							Editor editor = sp.edit();
							editor.putBoolean("isShown", false);
							editor.commit();
							dialog.dismiss();
						}

					});
			customBuilder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			ImageView startImage = new ImageView(this);
			String url = response.getString("image");
			System.out.print(url);
			if (!url.equals("")) {
				StartImageLoader.setValue(dialog, customBuilder);
				StartImageLoader.loadImage(this, startImage, url);
			} else {
				customBuilder.create().show();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onDestroy() {
		Session.setInstance(null);
		// SharedPreferences prefs = getSharedPreferences(
		// Constants.KEY_SESSION_PREFS, 0);
		// int uid = prefs.getInt(Constants.KEY_UID, 0);
		//
		// Editor editor = prefs.edit();
		// editor.putString(Constants.KEY_USER_NAME, "");
		// editor.putBoolean(Constants.KEY_IS_LOGINED, false);
		// editor.putInt(Constants.KEY_UID, 0);
		// editor.commit();
		//
		// AsyncHttpClient client = new AsyncHttpClient();
		// client.get(Config.URL + "/login.php?logout=" + uid,
		// new JsonHttpResponseHandler() {
		// @Override
		// public void onSuccess(JSONObject response) {
		// Log.v("LOGOUT", response.toString());
		// }
		// });
		// PushService.actionStop(getApplicationContext());
		super.onDestroy();
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Session.setInstance(null);
						MainActivity.this.finish();
						// android.os.Process.killProcess(android.os.Process
						// .myPid());

					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		boolean isAutoPush = getSharedPreferences(PushService.TAG, MODE_PRIVATE)
				.getBoolean("isAutoPush", false);
		if (isAutoPush) {
			// boolean isStarted = getSharedPreferences(PushService.TAG,
			// android.content.Context.MODE_PRIVATE).getBoolean(
			// "isStarted", false);
			// if (!isStarted) {
			PushService.actionStart(getApplicationContext());
			System.out.print("start");
			// }
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return false;
		}
		return false;
	}
}
