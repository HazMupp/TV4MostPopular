package com.peter.myapp.tv4mostpopular;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetJsonTask extends AsyncTask<String, String, String> {

    Context context;
    LayoutInflater inflater;
    View inflatedLayout;
    ArrayList<Movie> titles;
    JSONArray jArray;
    JSONObject parentObject;
    JSONObject finalObject;
    JSONArray parentArray;
    StringBuffer finalBufferedData;
    String title;
    InputStream inputStream;
    String titleItem;
    String itemDescItem;
    String dateTimeItem;
    static Bitmap bmp;
    String imageLink;
    String result = null;
    Drawable d; // Don't touch the "d"

    public ArrayList<Movie> getTitles() {
        return titles;
    }

    public GetJsonTask(Context context) { //Constructor (very important)

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflatedLayout = inflater.inflate(R.layout.activity_json_items, null, false);
        titles = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Any preparations?
    }

    @Override
    protected String doInBackground(String... params) {


        // The actual AsyncTask
        HttpURLConnection connection = null;
        BufferedReader reader = null;

            try {
                //Url link provided by Bonnier Broadcasting, See MainActivity
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000); //
                connection.setConnectTimeout(10000); // If you cant connect within this amount of time you probably A) got no service at all, OR   B)are connected to wifi through ComHem ADSL
                connection.connect();
                //The actual "value" of the connection
                inputStream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                result = buffer.toString();

                if (result == null) {
                    Toast.makeText(context, "FAIIIL1", Toast.LENGTH_SHORT).show();
                }

                parentObject = new JSONObject(finalJson);
                parentArray = parentObject.getJSONArray("results"); // The unique key for "everything" in this particular json

                finalBufferedData = new StringBuffer();

                for (int i = 0; i < parentArray.length(); i++) {

                    finalObject = parentArray.getJSONObject(i);

                    title = finalObject.getString("title");
                    dateTimeItem = finalObject.getString("broadcast_date_time");
                    imageLink = finalObject.getString("image");
                    bmp = getBitmapFromURL(imageLink); // The link in the json to the actual image
                    itemDescItem = finalObject.getString("description");
                    d = new BitmapDrawable(Resources.getSystem(), bmp);
                    // Now, a new item in the list is created
                    newMovie();

                    // End of loop
                }

                //return buffer.toString(); //If we are able to get the data
                return finalBufferedData.toString();    //If we are able to get the data


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) { // If connection is null, there is no need to close it
                    connection.disconnect();
                }
                try {
                    if (reader != null) { // If reader is null, there is no need to close it either
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        return null; // If we are not able to get anything
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        MainActivity.createList(); // You found me
        JSONObject jObject = null;

        try {
            jObject = new JSONObject(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        titleItem = null;
        //   itemDescItem = null;
        dateTimeItem = null;
        // imageItem = null;

        jArray = null;
        try {
            jArray = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }//////////////////////////////////////////////////


        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject oneObject = jArray.getJSONObject(i);
                // Pulling items from the array
                // titleItem = oneObject.getString("title");
                //          dateTimeItem = oneObject.getString("broadcast_date_time");
                //   itemDescItem = oneObject.getString("description");

                // imageLink = oneObject.getString("image"); // Can this specific step be done earlier, that would be great. #OfficeSpace
                // imageItem = oneObject.getString("image");
                //imageItem = setBitmapFromUrl(imageLink);   // Will trigger Androids BlockGuardPolicy if used here.

                //    MyCustomAdapter.LoadImageFromWebOperations(imageLink);
            } catch (JSONException e) {
                // Oops
                titleItem = "fail " + i;
                e.printStackTrace();
            }
        }
    }

            // Should only be called in the doInBackground() method
    public void newMovie() {
        Movie movie = new Movie(title, dateTimeItem, imageLink, d, itemDescItem);
        titles.add(movie);
    }

    // Retrieves image as a bitmap from the provided url
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }
}
