package org.aakashlabs.gresyns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final String boxlist[] = {"HF","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	public static final String[] from = new String[] { "title" };
    public static final int[] to = new int[] { R.id.title };
	private Intent listIntent;
    ListView listview;
	public static List<ListItem> list=new ArrayList<ListItem>();
	public static List<ListItem> savlist=new ArrayList<ListItem>();
	static List<String> data;
	static List<HashMap<String, Object>> fillMaps;
    Button boxListButton;
    Button groupListButton;
    Button favButton;
    View progressbar;
    private Boolean isBoxList = true;
    private Boolean isGroupList = false;
    private Boolean isFavList = false;
    private Boolean isLoaded = false;
    private DBManager db;
    private static ProgressDialog progDailog;
    Thread t;
    private Handler progressHandler ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progDailog=new ProgressDialog(MainActivity.this);
		progDailog.setTitle("Please Wait!!");
		progDailog.setMessage("Wait!!");
		progDailog.setCancelable(false);
		progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		setContentView(R.layout.activity_main);
		
		boxListButton = (Button) findViewById(R.id.boxlistButton);
		groupListButton = (Button) findViewById(R.id.grouplistButton);
		favButton = (Button) findViewById(R.id.favButton);
		listview = (ListView) findViewById(R.id.combined_list);
		listview.setFastScrollEnabled(true);
		

		toBox();
		
		groupListButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	if(isBoxList)
		    	{
		    		boxListButton.setBackgroundColor(getResources().getColor(R.color.light));
		    		groupListButton.setBackgroundColor(getResources().getColor(R.color.dark));
		    		boxListButton.setTextColor(Color.BLACK);
		    		groupListButton.setTextColor(Color.WHITE);
		    		toGroup();
		    		isGroupList=true;
		    		isBoxList=false;
		    	}
		    	
		    	else if(isFavList)
		    	{
		    		favButton.setBackgroundColor(getResources().getColor(R.color.light));
		    		groupListButton.setBackgroundColor(getResources().getColor(R.color.dark));
		    		favButton.setTextColor(Color.BLACK);
		    		groupListButton.setTextColor(Color.WHITE);
		    		toGroup();
		    		isGroupList=true;
		    		isFavList=false;
		    	}
		    }
		});

		boxListButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	if(isGroupList)
		    	{
		    		groupListButton.setBackgroundColor(getResources().getColor(R.color.light));
		    		boxListButton.setBackgroundColor(getResources().getColor(R.color.dark));
		    		groupListButton.setTextColor(Color.BLACK);
		    		boxListButton.setTextColor(Color.WHITE);
		    		toBox();
		    		isBoxList=true;
		    		isGroupList=false;
		    	}
		    	
		    	else if(isFavList)
		    	{
		    		favButton.setBackgroundColor(getResources().getColor(R.color.light));
		    		boxListButton.setBackgroundColor(getResources().getColor(R.color.dark));
		    		favButton.setTextColor(Color.BLACK);
		    		boxListButton.setTextColor(Color.WHITE);
		    		toBox();
		    		isBoxList=true;
		    		isFavList=false;
		    	}
		    }
		});

		favButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	if(isGroupList)
		    	{
		    		groupListButton.setBackgroundColor(getResources().getColor(R.color.light));
		    		favButton.setBackgroundColor(getResources().getColor(R.color.dark));
		    		groupListButton.setTextColor(Color.BLACK);
		    		favButton.setTextColor(Color.WHITE);
		    		toFav();
		    		isFavList=true;
		    		isGroupList=false;
		    	}
		    	
		    	else if(isBoxList)
		    	{
		    		boxListButton.setBackgroundColor(getResources().getColor(R.color.light));
		    		favButton.setBackgroundColor(getResources().getColor(R.color.dark));
		    		boxListButton.setTextColor(Color.BLACK);
		    		favButton.setTextColor(Color.WHITE);
		    		toFav();
		    		isFavList=true;
		    		isBoxList=false;
		    	}
		    }
		});
		
		
		progressHandler = new Handler() 
		 {

		     public void handleMessage(Message msg1) 
		     {
		    	 if(progDailog.isShowing())
		         {
		    		 progDailog.dismiss();
		    		 toGroup();
		         }
		         	
		         //toGroup();
		         super.handleMessage(msg1);
		     }
		 };
		if(!isLoaded)
			{
			t=new Thread ( new Runnable()
		{
			public void run() {
				
			 	
			    db=new DBManager(getApplicationContext());
				db.open();
				
				list = db.getGroupList();
				savlist=list;
				//isLoaded=true;
				db.close();
				isLoaded=true;   
				//if(progDailog.isShowing())
					//progDailog.dismiss();
				progressHandler.sendEmptyMessage(0);
				//Log.d("DATA","data loaded");
				//toGroup();
					
		    }
		});
		t.start();
			}
			
		}
    
	private void toBox()
	{

		try{		
			listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, boxlist));
			}
		
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
			Toast.makeText(getApplicationContext(), "Error: "+e.toString(), Toast.LENGTH_LONG).show();				
		}
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
					listIntent=new Intent(getApplicationContext(), Box.class);
					listIntent.putExtra("box",boxlist[position]);
					startActivity(listIntent);
				  }
				}); 
	}
				
	private void toGroup()
	{
		if(!isLoaded){
		progDailog.show();


		}
		
		else
		{
			fillMaps = new ArrayList<HashMap<String, Object>>();
			for(ListItem li:list){
				HashMap<String, Object> map = new HashMap<String, Object>();
				String data=li.getGloss();
				String mean[]=data.split(";");
			    map.put("title",mean[0]);
	
			    fillMaps.add(map);	
			}
			SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.row, from, to);
		    listview.setAdapter(adapter);
		}				    

	    listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long id) {
				
				ListItem li=list.get(position);
				
				try{
					
					listIntent=new Intent(getApplicationContext(), GroupDisplay.class);
					listIntent.putExtra("sid",li.getSID());
					listIntent.putExtra("gloss",li.getGloss());
					startActivity(listIntent);
					
				}
				catch(Exception e)
				{e.printStackTrace();}
			}
			
		});
				
	}
		
	private void toFav()
	{
		db.open();
		data=db.getFavList();
		listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data));
		db.close();

		listview.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
				  
					String item=parent.getItemAtPosition(position).toString();
					listIntent=new Intent(getApplicationContext(), DisplayActivity.class);
					listIntent.putExtra("ID",item);
					listIntent.putExtra("caller","MainActivity");
					startActivity(listIntent);
				  }
				}); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.action_about:
			Intent ourIntent=new Intent(this,About.class);
			startActivity(ourIntent);
            return true;
  
        default:
            return super.onOptionsItemSelected(item);
        }
    }    

}
