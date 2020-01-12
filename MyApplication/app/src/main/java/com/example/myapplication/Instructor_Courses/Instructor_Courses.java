package com.example.myapplication.Instructor_Courses;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.myapplication.Course;
import com.example.myapplication.ListCoursesAdapter;
import com.example.myapplication.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class Instructor_Courses extends AppCompatActivity {
    RecyclerView rv;

    Integer[] drawableArray; /* = {R.drawable.logo, R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo,R.drawable.logo, R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo};*/ //To be replaced by Select Icons
    int[] coursesId;
    String[] titleArray;// = {"CMPS 272","CMPS 297R","CMPS 297N"};
    String[] subtitleArray;// = {"Operating System","Cloud Computing","Mobile Development"};
    String[] subtitleArray2;// = {"MWF 9:00-9:50","MWF 2:00-2:50","MWF 3:00-3:50"};
    String[] subtitleArray3;// = {"Nicely 325","Bliss 205","Bliss 105"};
    ListCoursesAdapter ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        courses s = new courses();
        s.instructorID = 26;

        Gson gson = new Gson();
        try {
            s.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Course[] courseslis;
        courseslis = gson.fromJson(s.res, Course[].class); //Res refers to result
        drawableArray = new Integer[courseslis.length];
        titleArray = new String[courseslis.length];
        subtitleArray = new String[courseslis.length];
        subtitleArray2 = new String[courseslis.length];
        subtitleArray3 = new String[courseslis.length];
        coursesId = new int[courseslis.length];

        for (int i=0; i<courseslis.length; i++){
            coursesId[i] = courseslis[i].getCourseId();
            drawableArray[i] = R.drawable.logo;
            titleArray[i] = courseslis[i].getSubject();
            subtitleArray[i] = courseslis[i].getSubject();
            subtitleArray2[i] = courseslis[i].getDays() + " " + courseslis[i].getTime();
            subtitleArray3[i] = courseslis[i].getBuilding() + " " +courseslis[i].getRoom();
        }

        rv = (RecyclerView)findViewById(R.id.rv);
        ad = new ListCoursesAdapter(Instructor_Courses.this,drawableArray,titleArray,subtitleArray,subtitleArray2,subtitleArray3, coursesId);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setAdapter(ad);

    }
}

class courses extends AsyncTask<URL, Integer, Void> {
    public  int instructorID;
    public String res;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getCourses(int instructorID) throws Exception {

        String url = "https://nfcattend.azurewebsites.net/api/v1/Courses?code=Fam740jszxGiICV04FahnqzDQH7/1BXWmgpNaTycYSLOs3rhnamJxQ==&Instructor=" + instructorID;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add request header
        con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 4.01; Windows NT)");
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        // Send post request
        //int responseCode = con.getResponseCode();
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
