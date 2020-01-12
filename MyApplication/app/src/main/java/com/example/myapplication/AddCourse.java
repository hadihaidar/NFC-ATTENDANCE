package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class AddCourse extends Activity {
    Integer[] drawableArray;
    String[] titleArray;
    String[] subtitleArray;
    String[] subtitleArray2;
    String[] subtitleArray3;
    Integer[] courseId;
    String instID;
    String name;
    String aubnet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();    //get the intent
        instID = intent.getStringExtra("instructorID");
        name  = intent.getStringExtra("name");

        aubnet  = intent.getStringExtra("aubnet");
        RecyclerView rv;
        TBA x  = new TBA();
        x.instructorID = -1;
        try{

        x.execute().get();
        }catch (Exception e){

        }

        String json = x.res;
            Course[] courseslis;

            Gson gson = new Gson();
            courseslis = gson.fromJson(json, Course[].class); //Res refers to result
            drawableArray = new Integer[courseslis.length];
            titleArray = new String[courseslis.length];
            subtitleArray = new String[courseslis.length];
            subtitleArray2 = new String[courseslis.length];
            subtitleArray3 = new String[courseslis.length];
            courseId  = new Integer[courseslis.length];
            for (int i=0; i<courseslis.length; i++){
                drawableArray[i] = R.drawable.logo;
                titleArray[i] = courseslis[i].getSubject();
                subtitleArray[i] = courseslis[i].getTitle();
                subtitleArray2[i] = courseslis[i].getDays() + " " + courseslis[i].getTime();
                subtitleArray3[i] = courseslis[i].getBuilding() + " " +courseslis[i].getRoom();
                courseId[i] = courseslis[i].getCourseId();
            }

        TBACourseAdapter ad;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_course);
            rv = (RecyclerView)findViewById(R.id.rv);
            ad = new TBACourseAdapter(AddCourse.this,drawableArray,titleArray,subtitleArray , subtitleArray2, subtitleArray3, instID, courseId ,name,aubnet);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setHasFixedSize(true);
            rv.setAdapter(ad);

    }
}

class TBA extends AsyncTask<URL, Integer, Void> {
    public int instructorID;
    public String res;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getCourses(int instructorID) throws Exception {

        String url = "https://nfcattend.azurewebsites.net/api/v1/Courses?code=Fam740jszxGiICV04FahnqzDQH7/1BXWmgpNaTycYSLOs3rhnamJxQ==&Instructor=-1";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add reuqest header
        con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 4.01; Windows NT)");
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        // Send post request
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {

            StringBuffer response = new StringBuffer();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //print result
            in.close();
            String x = response.toString();
            return (response.toString());

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(URL... urls) {
        try {
            res = getCourses(instructorID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
class ChangeInstructorID extends AsyncTask<Void, Integer, String> {
    public String newInstructorId;
    public int courseId;
    public String name;


    private static String changeInstructorID(String newInstructorId, int courseId, String name) throws Exception {

        String url = "https://nfcattend.azurewebsites.net/api/v1/setcourse?code=vJxUDJOhzM9AvPAyezjonRnZaJZzPziEYI1d9hRWVSQdnFHPfFPfHw==";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String urlParameters = "courseid=" + Integer.toString(courseId) + "&instructorid=" + newInstructorId + "&name="+name;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        return (response.toString());

    }

    @Override
    protected String doInBackground(Void... urls) {
        String result = "";
        try {
            result = changeInstructorID(newInstructorId, courseId,name);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
