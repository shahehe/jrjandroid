package gov.jrj.library.http;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONRPCHandler {
	public void onSuccess(String str){}
	public void onSuccess(JSONObject json){};
	public void onSuccess(JSONArray json){};
	public void onFailed(String message){};
	public void onError(String message){};
	public void onStart(){};
	public void onFinish(){};
}
