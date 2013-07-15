package org.aakashlabs.gresyns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayActivity extends Activity {
public static String word;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent=getIntent();
		word=intent.getStringExtra("ID");
        setTitle(word);
		setContentView(R.layout.activity_display);

		TextView wordview=(TextView)findViewById(R.id.word);
		TextView greview=(TextView)findViewById(R.id.gremeaning);
		
		wordview.setText(word);

        final ListView listview = (ListView) findViewById(R.id.meanings);

		DBManager db=new DBManager(this);
		db.open();
		
		String gremeaning = db.getGREMeaning(word);
		greview.setText(gremeaning);
		
        List<String[]> data = db.getWord(word);
        db.close();
        
        List<String> meaning = new ArrayList<String>();
        Iterator<String[]> itr = data.iterator();
		String[] item;

        while(itr.hasNext())
        {
        	item=itr.next();
    		item[1]=item[1].replace("; "+'\"',";\n"+'\"');
        	meaning.add(item[0]+"  "+item[1]);
        	
        }

		listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, meaning));
		Button b=(Button)findViewById(R.id.bview);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent("org.aakashlabs.gresyns.VIEWWORDLIST");
				//i.putExtra("word",word);
				startActivity(i);
			}
		});

	}


}
