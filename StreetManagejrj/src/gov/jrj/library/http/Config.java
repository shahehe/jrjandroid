package gov.jrj.library.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class Config {
	//public static final String URL = "http://demo.bctcom.net/fdemo";
	public static  String URL = "http://218.249.192.55/jrj";
	public static final String API_URL = "http://218.249.192.55/jrj/api.php";	
//	public static final String URL = "http://64.150.161.193/jrj";
//	public static final String API_URL = "http://64.150.161.193/jrj/api.php";	

	public static String getMD5(String string){
		byte[] hash;
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }
	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        if ((b & 0xFF) < 0x10) hex.append("0");
	        hex.append(Integer.toHexString(b & 0xFF));
	    }
	    return hex.toString(); 
	}
	
	public static String getCacheData(Context context,String url){
		String md5 = Config.getMD5(url);
		File file = new File(context.getCacheDir() +"/"+md5);
		if(!file.exists()) return null;
		String res = null; 
		try{ 
			FileInputStream fin = new FileInputStream(file); 
			int length = fin.available(); 
			byte [] buffer = new byte[length]; 
			fin.read(buffer);     
			res = EncodingUtils.getString(buffer, "UTF-8"); 
			fin.close();     
        } catch(Exception e){ 
        	e.printStackTrace(); 
        } 
        return res;
	}
	
	public static void setCacheData(Context context,String url,String data){
		String md5 = Config.getMD5(url);
		File file = new File(context.getCacheDir() +"/"+md5);
		try{ 
			FileOutputStream fout = new FileOutputStream(file);
	        byte [] bytes = data.getBytes(); 
	        fout.write(bytes); 
	        fout.close(); 
		} catch(Exception e){ 
			e.printStackTrace(); 
		} 
	}
	
	public static void clearCacheData(Context context,String url){
		String md5 = Config.getMD5(url);
		File file = new File(context.getCacheDir() +"/"+md5);
		if(file.exists()){
			file.delete();
		}
	}
	
	  public static boolean delAllFile( String path) {
	       boolean flag = false;
	      
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	             //delFolder(path + "/" + tempList[i]);//再删除空文件夹
	             flag = true;
	          }
	       }
	       return flag;
	     }
	public static String readRawData(Context context,int rid){
		String res = null; 
		try {
			InputStream is = context.getResources().openRawResource(rid); 
			int length = is.available(); 
			byte [] buffer = new byte[length]; 
			is.read(buffer);     
			res = EncodingUtils.getString(buffer, "UTF-8"); 
			
		} catch (Exception e) {
            
		}
		return res;
	}	
	public static boolean isConnected(Context context){
		
		
		ConnectivityManager conMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //mobile 3G Data Network
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        //wifi
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if(mobile==State.CONNECTED || wifi == State.CONNECTED){
        	return true;
        }
        return 	isNetworkAvailable(context);
        
	}	
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isConnected = false;
		if (connectivity == null) {
			// 
		} else {//获取所有网络连接信息
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {//逐一查找状态为已连接的网络
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						isConnected = true;
						break;
					}
				}
			}
		}
		return isConnected;
	}	
}
