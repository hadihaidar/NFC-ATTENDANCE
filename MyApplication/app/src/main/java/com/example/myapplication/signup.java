package com.example.myapplication;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class signup extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button b = findViewById(R.id.login);

        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText editText = (findViewById(R.id.pass));
                String pass1 = editText.getText().toString();
                editText = findViewById(R.id.pass2);
                String pass2 = editText.getText().toString();
                EditText u = findViewById(R.id.username);
                String user = u.getText().toString();

                sign credentials = new sign();
                credentials.aubnet = user;
                credentials.password = pass1;
                try{
                    credentials.execute().get();
                }
                catch(Exception e ){}
                String result = credentials.result;

                if (pass1.equals(pass2)){
                    Gson gson = new Gson();
                    Response i = gson.fromJson(result, Response.class);
                    if(i.getSuccess().equals("false")){
                        Toast.makeText(signup.this, i.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent myIntent = new Intent(signup.this, MainActivity.class);
                        Toast.makeText(signup.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                        startActivity( myIntent);
                        finish();

                    }
                }
                else{
                    Toast.makeText(signup.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


class sign extends AsyncTask<Void, Integer, String> {
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

        String url = "https://nfcattend.azurewebsites.net/api/SignUp?code=OF8IIxSDF4WBYd6ExrfJG3Tv/7Af6ujJfaeCT1EB7dJN5v8DSmA4Gg==";
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
