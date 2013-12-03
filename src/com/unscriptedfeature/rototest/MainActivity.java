package com.unscriptedfeature.rototest;

import java.io.IOException;
import java.io.InputStream;
 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	String url = "http://games.espn.go.com/fhl/standings?leagueId=16080&seasonId=2014";
	ProgressDialog mProgressDialog;
	int teamCount = 12;
	int counter = teamCount+5;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        TextView txtDebug = (TextView) findViewById(R.id.debugtxt);
		txtDebug.setText("Debug Info");
		// Locate the Buttons in activity_main.xml
	    Button teamsbutton = (Button) findViewById(R.id.teamsbutton);
	    Button teamlistbutton = (Button) findViewById(R.id.teamlistbutton);
	    teamsbutton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
	    		new Teams().execute();
			}
		});
	    
	    teamlistbutton.setOnClickListener(new OnClickListener() {
	    	
	    	public void onClick(View v) {
	    		new TeamList().execute();
	    	}
	    });

	}

    // Teams AsyncTask
    private class Teams extends AsyncTask<Void, Void, Void> {
        String teamstxt;
    	String delimiter = "TR[bgcolor]";
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Roto Test");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
            	Document doc = Jsoup.connect(url).get();
                // Using Elements to get the Meta data
                Elements trs = doc.select(delimiter);
                int i = 1;
                if (counter == (teamCount*2)+6)
                	counter = teamCount + 5;
                for (Element tr : trs) {
                	if (i == counter)
                		teamstxt = tr.text();
                	i++;
                }
                counter++;
            }  catch (IOException e) {
               e.printStackTrace();
            }
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
            TextView txtTeams = (TextView) findViewById(R.id.teamstxt);
            txtTeams.setText(teamstxt);
            TextView txtDebug = (TextView) findViewById(R.id.debugtxt);
    		txtDebug.setText(String.valueOf(counter));
            mProgressDialog.dismiss();
        }
    }	

    // TeamList AsyncTask
    private class TeamList extends AsyncTask<Void, Void, Void> {
        String teamstxt;
    	String delimiter = "A[title]";
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Roto Test");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
            	Document doc = Jsoup.connect(url).get();
                // Using Elements to get the Meta data
                Elements alist = doc.select(delimiter);
                teamstxt = alist.text();
            }  catch (IOException e) {
               e.printStackTrace();
            }
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
            TextView txtTeams = (TextView) findViewById(R.id.teamslisttxt);
            txtTeams.setText(teamstxt);
            mProgressDialog.dismiss();
        }
    }	    
    
}
