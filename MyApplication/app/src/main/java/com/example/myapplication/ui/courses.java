package com.example.myapplication.ui;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class courses extends AsyncTask<URL, Integer, Void> {
    public int instructorID;
    public String res;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getCourses(int instructorID) throws Exception {

        String url = "https://nfcattend.azurewebsites.net/api/v1/Courses?code=Fam740jszxGiICV04FahnqzDQH7/1BXWmgpNaTycYSLOs3rhnamJxQ==&Instructor=" + instructorID;
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