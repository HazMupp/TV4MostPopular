package com.peter.myapp.tv4mostpopular;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    static ListView item;

    // ListView mListView;
    ArrayList<HashMap<String, String>> cList;
    ArrayAdapter<String> adapter;
    TextView itemName;
    LayoutInflater inflater;
    static MyCustomAdapter myCustomAdapter;
    static ListView myListView;
    ImageView imageIcon;
    static ArrayList<Movie> allTitles;
    public static boolean isInternetAvailable;
    RelativeLayout mainParent;

    public static String url = "http://webapi.tv4play.se/play/video_assets/most_viewed";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemName = (TextView) findViewById(R.id.JsonItemName);
        imageIcon = (ImageView) findViewById(R.id.JsonImage);
        item = (ListView) findViewById(R.id.listView);
        inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myListView = (ListView) findViewById(R.id.listView);
        mainParent = (RelativeLayout) findViewById(R.id.mainScrollView);

        isInternetAvailable = isNetworkAvailable();

        if (isInternetAvailable == true) { // If there is internet, continue as planned

            GetJsonTask getJsonTask = new GetJsonTask(this);
            getJsonTask.execute(url);

            //  new GetJsonTask(this).execute("http://webapi.tv4play.se/play/video_assets/most_viewed"); //The url relevant for the GetJsonTask.java class
            allTitles = getJsonTask.getTitles();
            myCustomAdapter = new MyCustomAdapter(this, R.id.ItemViewGroup, allTitles); // All the titles we got


            // TODO IMPORTANT!!  The actual ListView is created in the async class GetJsonTask.java in the onPostExecute() method, line 148. This is to prevent crash of the app because of some non-existent notifyDataSetChanged crap. The CountDownTimer below may be used instead to speed things up but may cause the app to crash.


          // new CountDownTimer(2000, 1000) { // To give the GetJsonTask some extra time to work. The opinions (my opinions) about the...
          //     // ...necessity of this may be subject to change.
          //     public void onTick(long millisUntilFinished) {

          //     }
          //     public void onFinish() {

          //    //     myListView.setAdapter(myCustomAdapter);


          //         //    myListView.
          //     }
          // }.start();

        } else {
                // If no internet connection
            Snackbar snackbar = Snackbar.make(mainParent, "No internet", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Basically restart the whole app.
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });
            snackbar.show();
        }
    }

    public static void createList(){
        myListView.setAdapter(myCustomAdapter);
    }

    protected void onStart(){
        super.onStart();

    }

    @Override // If you click on any item in the listView
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
