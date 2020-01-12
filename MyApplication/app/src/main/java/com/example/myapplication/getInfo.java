package com.example.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type ;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.io.InputStream;

import com.example.myapplication.ui.Activity2;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;

import static com.github.mikephil.charting.components.Legend.LegendPosition.BELOW_CHART_CENTER;


public class getInfo extends Activity {
    final int blackcolor = Color.parseColor("#000000");
    final int redColor = Color.parseColor("#cc0000");
    Bitmap p ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_info);
        CircularImageView img = (CircularImageView) findViewById(R.id.profile);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(blackcolor);
        colors.add(redColor);


        TextView ProfileName = (TextView) findViewById(R.id.name);

        Intent intent = getIntent();    //get the intent
        String Name = intent.getStringExtra("Full Name");
        String rfid = intent.getStringExtra("RFID");
        String cID  = intent.getStringExtra("courseID");
        ProfileName.setText(Name);
        String studentsString = intent.getStringExtra("array");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Student>>() {
        }.getType();
        ArrayList<Student> studentList = gson.fromJson(studentsString, type);


        TextView ian = (TextView) findViewById(R.id.IAN);
        TextView email = (TextView) findViewById(R.id.mail);
        String photo = "";
        Image i = new Image();
        for (Student s : studentList) {
            if (s.getStudentRFID().equals(rfid)) {
                String mailto = (s.getAubNet() + "@mail.aub.edu");
                ian.setText("IAN: " + s.getIan());

                i.url = s.getPhoto();
                try {
                    i.execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ImageView l  = findViewById(R.id.profile);
                p=i.image;
                l.setImageBitmap(i.image);
                email.setText(Html.fromHtml("<a href=mailto:" + mailto + ">" + mailto + "</a>"));
                email.setMovementMethod(LinkMovementMethod.getInstance());


            }


        }

        getStatus s = new getStatus();

        s.courseid = Integer.parseInt(cID);
        s.studID = rfid;

        Gson gs = new Gson();
        try {
            s.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Statuss statusses = new Statuss();
        try {

            statusses = gs.fromJson(s.res, Statuss.class);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        PieChart pieChart = findViewById(R.id.piechart);
        pieChart.setDescription("");
        pieChart.setNoDataText("You have not conducted any class sessions yet!");
        Paint pa = pieChart.getPaint(Chart.PAINT_INFO);
        pa.setTextSize(48f);
        ArrayList<Entry> NoOfEmp = new ArrayList<Entry>();

//            pieChart.setNoDataText("No Data Available");
//            pieChart.invalidate();
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ((int) value) + "";
            }
        };
        PieDataSet dataSet;

        if((statusses.getSkipped() != 0 || statusses.getAttended() != 0)){
            NoOfEmp.add(new Entry(statusses.getSkipped(), 0));      //get Data from Db
            NoOfEmp.add(new Entry(statusses.getAttended(), 1));
             dataSet = new PieDataSet(NoOfEmp, "");


            Legend legend = pieChart.getLegend();
            dataSet.setValueFormatter(formatter);
            legend.setPosition(BELOW_CHART_CENTER);
            ArrayList<String> year = new ArrayList<String>();
            dataSet.setValueTextSize(13f);
            dataSet.setValueTextColor(Color.WHITE);
            year.add("Absent");
            year.add("Present");
            PieData data = new PieData(year, dataSet);

            pieChart.setData(data);
            dataSet.setColors(colors);
            pieChart.animateXY(5000, 5000);}
        else{
            if (!NoOfEmp.isEmpty()){
                NoOfEmp.remove(0);
                NoOfEmp.remove(1);

        }}

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getInfo.this);
                View mView = getLayoutInflater().inflate(R.layout.make_image_big, null);
                Intent intent = getIntent();    //get the intent
                String rfid = intent.getStringExtra("RFID");
                String studentsString = intent.getStringExtra("array");

                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Student>>() {
                }.getType();
                Image i = new Image();
                ArrayList<Student> studentList = gson.fromJson(studentsString, type);
                for (Student s : studentList) {
                    PhotoView photoView = mView.findViewById(R.id.imageView);

                    if (s.getStudentRFID().equals(rfid)) {
                        if (!s.getPhoto().equals("")) {
                            i.url = s.getPhoto();
                            photoView.setImageBitmap(p);
                            //photoView.setImageBitmap(i.image);
                        } else {
                            photoView.setImageResource(R.drawable.man);
                        }
                    }


                }

                // photoView.setImageResource(R.drawable.man);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                // Toast.makeText(getInfo.this, "active", Toast.LENGTH_SHORT).show();
            }
        });

        email.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.envelope2, 0, 0, 0);

    }

}




    class getStatus extends AsyncTask<URL, Integer, Void> {
        public int courseid;
        public  String studID;
        public String res;
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private static String getStudents(int courseid ,String studID) throws Exception {

            String url = "https://nfcattend.azurewebsites.net/api/v1/GetStats/"+courseid+"?code=nCcGaenAYLexQ1J5VUGArdfXsGY89hP5YBPhNYNbFviWcvCrb15jcA==&rfid="+ studID;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // add reuqest header
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
                return(response.toString());

            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(URL... urls) {
            try {
                res = getStudents(courseid,studID);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }



class Image extends AsyncTask<URL, Integer, Void> {
    public  String url;
    public Bitmap image;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static Bitmap getStudents(URL url) throws Exception {

        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        return image;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(URL... urls) {
        try {
            URL u = new URL(url);
            image =getStudents(u);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
