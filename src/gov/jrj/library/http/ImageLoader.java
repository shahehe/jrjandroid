package gov.jrj.library.http;

import gov.jrj.R;
import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.BinaryHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader{
	
	static final String TAG = "ImageLoader";
	private ImageView imageView;
	private String url;
	private Context mContext;
	//下载队列
	public Map<String,String> keys = new HashMap<String,String>();
	
	
	public static void loadImage(Context context, ImageView view,String file){
		if(file == null) return;
		try {
			AssetManager am = context.getAssets();
			InputStream in = am.open(file);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			view.setImageBitmap(bitmap);
			Log.v(TAG,"文1"+file);
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		File f = getCacheFile(context,file);
		Log.v(TAG,"文"+file);
		if(f.exists()){
			Bitmap bitmap = BitmapFactory.decodeFile(f.toString());
			if(bitmap == null){
				f.delete();
				view.setImageResource(R.drawable.download);
				ImageLoader il = new ImageLoader(context,view);
				il.start(file);
			}else{
				view.setImageBitmap(bitmap);
			}
		}else{
			view.setImageResource(R.drawable.download);
			ImageLoader il = new ImageLoader(context,view);
			il.start(file);
		}
		
	}
	
	public static BitmapDrawable getImageDrawable(Resources res,String file){
		return null;
	}
	
	public ImageLoader(){
		
	}
	
	public ImageLoader(Context context,ImageView view){
		this.imageView = view;
		this.mContext = context;
	}
	
	public void start(final String file){
		if(file == null || file.length() < 10 ) return;
		this.url = this.getUrl(file);
		if(!this.keys.containsKey(this.url)){
			AsyncHttpClient client = new AsyncHttpClient();
	        client.get(this.url, new BinaryHttpResponseHandler() {

				@Override
				public void onSuccess(byte[] binaryData) {
					try{
						Bitmap bitmap=BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
						if(bitmap != null){
							imageView.setImageBitmap(bitmap);
							File f = getCacheFile(mContext,file);
							if(!f.exists()){
								FileOutputStream fos;
								fos = new FileOutputStream(f);
								fos.write(binaryData);
					            fos.close();
							}
						}else{
							imageView.setImageResource(R.drawable.noimage);
						}
					}catch(Exception ex){
						imageView.setImageResource(R.drawable.noimage);
					}
				}
				
				@Override
				public void onStart() {
					Log.v(TAG,"文件开始下载...:"+url);
					keys.put(url, file);
				}

				@Override
				public void onFinish() {
					Log.v(TAG,"文件开始完成...:"+url);
					if(keys.containsKey(url)) keys.remove(url);
				}
	        });
		}else{
			Log.v(TAG,"正在下载中...:"+url);
		}
		
	}
	
	private String getUrl(String file){
		String link = String.format(Config.URL+"/image/%s",file);
		//String link = file;
		Log.v(TAG,link);
		return link;
	}
	
	public static File getCacheFile(Context context,String file){
		Log.v(TAG,"文件开始完成...:"+context.getCacheDir());
		return new File(context.getCacheDir()+"/"+file);
	}
}
