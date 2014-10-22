package gov.jrj.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



import com.example.androidhive.LazyAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import gov.jrj.R;
import gov.jrj.ui.util.Constants;

public class CustomizedListView extends Activity {
	// All static variables
	static final String URL = "http://api.androidhive.info/music/music.xml";
	// XML node keys
	static final String KEY_SONG = "Row"; // parent node
	static final String KEY_ID = "Column";
	public static final String KEY_TITLE = "Column2";
	public static final String KEY_ARTIST = "Column11";
	public static final String KEY_DURATION = "Column3";
	public static final String KEY_THUMB_URL = "thumb_url";
	public static final String KEY_POSITION = "Column12";
	public HashMap<String, String> pos2it = new HashMap<String,String>();
	/*
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_THUMB_URL = "thumb_url";*/
	TextView mTextTitle;
	ListView list;
    LazyAdapter adapter;
	public String getAssetsString(Context context,String fileName){
		StringBuffer sb = new StringBuffer();
		
		//根据语言选择加载
		try {
			AssetManager am = context.getAssets();
			InputStream in = am.open(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = reader.readLine())!=null){
				line += ("\n");
				sb.append(line);
			}

			reader.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public String getValue(Element fstElmnt, String tag)
	{
		// adding each child node to HashMap key => value
		NodeList nameList = fstElmnt.getElementsByTagName(tag);
		Element nameElement = (Element) nameList.item(0);
		nameList = nameElement.getChildNodes();
		return  nameList.item(0).getNodeValue();		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		setTitle();
		setBackBtnLisenter();
		final ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
/*
		XMLParser parser = new XMLParser();
		//String xml = getAssetsString(this,"book.xml");
		//String xml = parser.getXmlFromUrl(URL); // getting XML from URL
		String data = Config.readRawData(this,R.raw.book);
		Alerts.show(data,this);
		
		Document doc = parser.getDomElement(data); // getting DOM element*/
		try {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();		
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(getResources().getAssets().open("build.xml"));
		doc.getDocumentElement().normalize();
		NodeList nl = doc.getElementsByTagName(KEY_SONG);
		// looping through all song nodes <song>
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			Element fstElmnt = (Element) nl.item(i);
			pos2it.put(Integer.toString(i), getValue(fstElmnt, KEY_ID));
			map.put(KEY_ID, getValue(fstElmnt, KEY_ID));
			map.put(KEY_TITLE, getValue(fstElmnt, KEY_TITLE));
			map.put(KEY_ARTIST,getValue(fstElmnt, KEY_ARTIST));
			map.put(KEY_DURATION, getValue(fstElmnt, KEY_DURATION) );
			map.put(KEY_THUMB_URL, getValue(fstElmnt, KEY_ID));
			map.put(KEY_POSITION, getValue(fstElmnt, KEY_POSITION));
			/*
			map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
			map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
			map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
			map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));
			*/

			// adding HashList to ArrayList
			songsList.add(map);
		}
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}
		

		list=(ListView)findViewById(R.id.list);
		
		// Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, songsList);
        list.setAdapter(adapter);
		/*
		map.put(KEY_ID, "1");
		map.put(KEY_TITLE, "Someone Like You");
		map.put(KEY_ARTIST,"Adelefasdfasdafasdasdflsadkflsadmf xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxsadfmkadslf ksadlf ");
		map.put(KEY_DURATION, "4:47");
		map.put(KEY_THUMB_URL,"company");
		*/        

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(CustomizedListView.this, yuxiang.class);
				intent.putExtra(Constants.KEY_SUBJECT_NAME, getString(R.string.tab1));
				intent.putExtra(Constants.KEY_ITEM_NAME, getString(R.string.book));
				intent.putExtra(Constants.SUBJECT_TITLE, "西城区金融街街道办事处");
				intent.putExtra(Constants.CONTENT_FILE, "test.txt");
				intent.putExtra("buildingId", pos2it.get(Integer.toString(position)));
//				Toast.makeText(CustomizedListView.this,"buildingId:"+pos2it.get(Integer.toString(position)), 2000).show();
				intent.putExtra("position", songsList.get(position).get(KEY_POSITION));
				intent.putExtra("name", songsList.get(position).get(KEY_TITLE));
//				Toast.makeText(CustomizedListView.this,songsList.get(position).get(KEY_TITLE), 2000).show();
				CustomizedListView.this.startActivity(intent);

			}
		});		
	}	
	private void setTitle() {
		String subTitle = "",subject="";
		subTitle = getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
		subject = getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
		mTextTitle.setText(subject + " > " + subTitle);

	}

	private void setBackBtnLisenter() {
		Button backBtn = (Button) findViewById(R.id.about_back_btn);
		backBtn.setOnClickListener(new OnClickListener() {

			 
			@Override
			public void onClick(View v) {

				CustomizedListView.this.finish();
			}
		});
	} 

}