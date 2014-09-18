package gov.jrj;



import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;

import android.app.Application;
import android.location.Location;
import android.widget.Toast;

public class AppEx extends Application {

	// �ٶȿ���ƽ̨�����Ӧ����Ϣ
	// http://dev.baidu.com/wiki/static/imap/key/
	// Ӧ�����: StreetManage
	// Ӧ������: �ֵ���Ϣ����
	// Key: DA6FBE721D3CCAAAE837F12D77F12593F7B449ED
	public static final String APP_NAME = "StreetManage";
	public static final String KEY = "DA6FBE721D3CCAAAE837F12D77F12593F7B449ED";

	public BMapManager mBMapMan = null;
	private static AppEx app;
	private Location newLocation;

	public Location getNewLocation() {
		return newLocation;
	}

	public void setNewLocation(Location newLocation) {
		this.newLocation = newLocation;
	}

	public static synchronized AppEx getInstance() {
		return app;
	}

	@Override
	public void onCreate() {
		app = this;
		mBMapMan = new BMapManager(this);
		mBMapMan.init(KEY, new MyGeneralListener());
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}

	public static class MyGeneralListener implements MKGeneralListener {

		 
		@Override
		public void onGetNetworkState(int arg0) {
			Toast.makeText(app.getApplicationContext(), R.string.network_error,
					Toast.LENGTH_SHORT).show();
		}

		 
		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {// ��ȨKey����
				Toast.makeText(app.getApplicationContext(), R.string.key_error,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
