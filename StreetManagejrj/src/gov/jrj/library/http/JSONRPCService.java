package gov.jrj.library.http;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.AsyncHttpResponseHandler;
import gov.jrj.library.http.RequestParams;

public class JSONRPCService {
	private static String TAG = "JSON_RPC";
	private AsyncHttpClient client;
	private static JSONRPCService instance = null;
	
	private JSONRPCService() {
		this.client = new AsyncHttpClient();
	}
	
	public synchronized static JSONRPCService getInstance() {   
		if (instance == null) {   
			instance = new JSONRPCService();   
		}   
		synchronized (instance) {
			return instance;   
		}
	}
	
	public void uploader(Uri uri,final JSONRPCHandler handler){
		try{
			RequestParams params = new RequestParams();
			params.put("Filedata", new File(uri.getPath()));
			this.client.post(Config.API_URL,params,new AsyncHttpResponseHandler(){
				@Override
				public void onSuccess(String response) {
					Log.v(TAG,"JSONRPC Uploader Response:"+response);
					handler.onSuccess(response);
				}

				@Override
				public void onFailure(Throwable error) {
					handler.onError(error.getMessage());
				}

				@Override
				public void onStart() {
					handler.onStart();
				}

				
				@Override
				public void onFinish() {
					handler.onFinish();
				}
			});
		}catch(Exception ex){
			
		}
		
	}
	
	public void post(String method,Object[] params,final JSONRPCHandler handler){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("method", method);
			jObject.put("id",System.currentTimeMillis());
			jObject.put("jsonrpc","2.0");
			JSONArray jp = new JSONArray();
			if(params != null){
				for(Object o : params){
					jp.put(o);
				}
			}
			jObject.put("params", jp);
			Log.v(TAG,Config.API_URL);
			client.post(Config.API_URL,jObject,new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					Log.v(TAG,"JSONRPC Response:"+response);
					try{
						JSONObject json = new JSONObject(response);
						if(handler != null){
							if(json.has("result")){
								if(json.get("result").getClass().equals(JSONArray.class)){
									handler.onSuccess(json.getJSONArray("result"));
								}else{
									handler.onSuccess(json.getJSONObject("result"));
								}
							}else{
								handler.onFailed(json.getJSONObject("error").getString("message"));
							}
						}
					}catch(Exception ex){
						ex.printStackTrace();
						handler.onError(ex.getMessage());
					}
				}

				@Override
				public void onFailure(Throwable error) {
					handler.onError(error.getMessage());
				}

				@Override
				public void onStart() {
					handler.onStart();
				}

				
				@Override
				public void onFinish() {
					handler.onFinish();
				}
				
	        });
		} catch (JSONException e) {
			e.printStackTrace();
			handler.onError(e.getMessage());
		}
	}

	public static void exec(String method,Object[] params,JSONRPCHandler handler){
		JSONRPCService.getInstance().post(method,params,handler);
	}
	
	public static void upload(Uri uri,JSONRPCHandler handler){
		JSONRPCService.getInstance().uploader(uri, handler);
	}
}
