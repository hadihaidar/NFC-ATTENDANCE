package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import com.example.myapplication.ui.Activity2;
import com.google.gson.Gson;

import android.view.View;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button b = findViewById(R.id.login);

        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText editText = (findViewById(R.id.username));
                String user = editText.getText().toString();
                editText = findViewById(R.id.pass);
                String pass = editText.getText().toString();
                login credentials = new login();
                credentials.aubnet = user;
                credentials.password = pass;
                try{
                credentials.execute().get();
                }
                catch(Exception e ){}
                String result = credentials.result;
                Gson gson = new Gson();
                Instructor i = gson.fromJson(result, Instructor.class);
                if (i.getInstructorId()!=0){
                    Intent myIntent = new Intent(MainActivity.this, Activity2.class);
                    myIntent.putExtra("instructorID",Integer.toString( i.getInstructorId()) );
                    myIntent.putExtra("name",i.getName());
                    myIntent.putExtra("aubnet",i.getAubNet()+"@aub.edu.lb");
                    startActivity( myIntent);
                    finish();

                }
                else{
                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();

                }

            }
        });

        TextView text = findViewById(R.id.signup);

        text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
             Intent myIntent = new Intent(MainActivity.this, signup.class);
             startActivity( myIntent);

            }
        });
    }
    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}


class login extends AsyncTask<Void, Integer, String> {
    public String aubnet;
    public String password;
    public String result;

    public static String hash(String password) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String salt = "some_random_salt";
            String passWithSalt = password + salt;
            byte[] passBytes = passWithSalt.getBytes();
            byte[] passHash = sha256.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< passHash.length ;i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }
    private static String attend(String aubnet, String password) throws Exception {

        String url = "https://nfcattend.azurewebsites.net/api/SignIn?code=s5xADyH5Su3dSSzV4sNKw7SJgdhxTu3JAa5Nrg0aMtPb9ijF7Yelaw==";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept", "*/*");
        con.setRequestProperty("cache-control", "no-cache");
        String x = hash(password);
        String urlParameters = "aubnet=" + aubnet + "&password=" + x;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();


        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        return (response.toString());

    }

    @Override
    protected String doInBackground(Void... urls) {

        try {
            result = attend(aubnet,password);
        } catch (Exception e) {
            result = e.toString();
        }
        return result;
    }
}
