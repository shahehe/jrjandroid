package gov.jrj.ui;

import org.json.JSONObject;

import gov.jrj.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class ProductInfoActivity extends Activity {

	private Activity mContext;
	TextView mTextTitle;
	WebView webView;
	Button btnBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_info_activity);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		mTextTitle.setText("下架食品详情");
		webView = (WebView) findViewById(R.id.webView1);
		btnBack = (Button) findViewById(R.id.about_back_btn);
		initViews();
		showData();
	}
	
	private void initViews() {
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ProductInfoActivity.this.finish();
			}
		});
	}
	
	private void showData(){
		try{
			JSONObject data = new JSONObject(this.getIntent().getStringExtra("data"));
			TextView et1 = (TextView)this.findViewById(R.id.name);
			et1.setText(data.getString("name"));
			
			TextView et2 = (TextView)this.findViewById(R.id.brand);
			et2.setText(data.getString("brand"));
			
			TextView et3 = (TextView)this.findViewById(R.id.model);
			et3.setText(data.getString("model"));
			
			TextView et4 = (TextView)this.findViewById(R.id.batch);
			et4.setText(data.getString("batch"));
			
			TextView et5 = (TextView)this.findViewById(R.id.item);
			et5.setText(data.getString("item"));
			
			TextView et6 = (TextView)this.findViewById(R.id.company);
			et6.setText(data.getString("company"));
			
			TextView et7 = (TextView)this.findViewById(R.id.party);
			et7.setText(data.getString("party"));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void showDataHtml(){
		try{
			JSONObject data = new JSONObject(this.getIntent().getStringExtra("data"));
			webView.setBackgroundColor(0);
			StringBuffer html = new StringBuffer();
			html.append("<!DOCTYPE HTML><html><style>html{padding:0px;margin:0px;} dt{color:#666;font-size:12px;} dd{line-height:30px;font-size:18px;} h2{border-bottom:1px solid #ccc;text-align:center;}</style><body>");
//			html.append("<h2>"+data.getString("name")+"</h2>");
//			html.append("<dl>");
//			html.append("<dt>样品名称：</dt>");
//			html.append("<dd>"+data.getString("name")+"</dd>");
//			html.append("<dt>商标：</dt>");
//			html.append("<dd>"+data.getString("brand")+"</dd>");
//			html.append("<dt>规格型号：</dt>");
//			html.append("<dd>"+data.getString("model")+"</dd>");
//			html.append("<dt>生产批次：</dt>");
//			html.append("<dd>"+data.getString("batch")+"</dd>");
//			html.append("<dt>标注生产单位名称：</dt>");
//			html.append("<dd>"+data.getString("company")+"</dd>");
//			html.append("<dt>不合格项目：</dt>");
//			html.append("<dd>"+data.getString("item")+"</dd>");
//			html.append("<dt>当事人姓名或名称：</dt>");
//			html.append("<dd>"+data.getString("party")+"</dd>");
//			html.append("</dl>");
			//html.append("<h3>"+data.getString("name")+"</h3>");
			//html.append("<div>"+data.getString("company")+"</div>");
			html.append("<h2>"+data.getString("name")+"</h2>");
			html.append("<table><tbody>");
			html.append("<tr><td>商标</td><td>生产批次</td></tr>");
			html.append("<tr><td>规格型号</td><td>生产批次</td></tr>");
			html.append("<tr><td>生产批次</td><td>生产批次</td></tr>");
			html.append("<tr><td>不合格项</td><td>生产批次</td></tr>");
			html.append("<tr><td>生产单位</td><td>生产批次</td></tr>");
			html.append("<tr><td>当事人</td><td>生产批次</td></tr>");
			html.append("</tbody></table>");
			html.append("</body></html>");
			//序号	样品名称	商标	规格型号	生产批次	标注生产单位名称	不合格项目	当事人姓名或名称
			Log.v("t", html.toString());  
			webView.loadDataWithBaseURL(".", html.toString(), "text/html", "utf-8", "");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
}
