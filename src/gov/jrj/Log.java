package gov.jrj;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/*
 * Android Log wrapper
 */
public final class Log {
	// set to false when building release version
	public static final boolean DBG = true;
	public static final boolean LOG_IO = true;
	public static final boolean NO_DATA = false;//�ж�λ�ź� ��û�е�ͼ���
	public static boolean PRIVIEW = true; // ��ʾ ʱ�����û���ź� �Ƿ���ʾ�̶�λ�õĵ�ͼ
	
	public static final String APP_TAG = "StreetManage";

	@SuppressWarnings("rawtypes")
	public static void i(Object tag, String msg) {
		if (!DBG)
			return;

		if (tag != null) {
			if (tag instanceof String) {
				android.util.Log.i(APP_TAG, formatMsg((String) tag, msg));
			} else if (tag instanceof Class) {
				android.util.Log.i(APP_TAG, formatMsg(((Class) tag).getSimpleName(), msg));
			} else {
				android.util.Log.i(APP_TAG, formatMsg(tag.getClass().getSimpleName(), msg));
			}
		} else {
			android.util.Log.i(APP_TAG, msg);
		}

	}

	@SuppressWarnings("rawtypes")
	public static void d(Object tag, String msg) {
		if (!DBG)
			return;

		if (tag != null) {
			if (tag instanceof String) {
				android.util.Log.d(APP_TAG, formatMsg((String) tag, msg));
			} else if (tag instanceof Class) {
				android.util.Log.d(APP_TAG, formatMsg(((Class) tag).getSimpleName(), msg));
			} else {
				android.util.Log.d(APP_TAG, formatMsg(tag.getClass().getSimpleName(), msg));
			}
		} else {
			android.util.Log.d(APP_TAG, msg);
		}

	}

	public static void d(String msg) {
		if (!DBG)
			return;
		android.util.Log.d(APP_TAG, msg);
	}

	public static void e(Object tag, String msg) {
		if (tag != null) {
			if (tag instanceof String) {
				android.util.Log.e(APP_TAG, formatMsg((String) tag, msg));
			} else if (tag instanceof Class) {
				android.util.Log.e(APP_TAG, formatMsg(((Class) tag).getSimpleName(), msg));
			} else {
				android.util.Log.e(APP_TAG, formatMsg(tag.getClass().getSimpleName(), msg));
			}
		} else {
			android.util.Log.e(APP_TAG, msg);
		}
	}

	private Log() {
	}

	private static String formatMsg(String tag, String msg) {
		return tag + " - " + msg;
	}

	// Log on sdcard,Just for debug use
	public static void logIO(String tag, String msg) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String file = "/sdcard/" + APP_TAG + ".txt";
			java.io.File SDFile = new java.io.File(file);
			if (!SDFile.exists()) {
				try {
					SDFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				FileOutputStream outputStream = new FileOutputStream(file, true);
				outputStream.write((new Date().toLocaleString() + "[" + tag
						+ "]" + ": " + msg + "\r\n").getBytes());
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
