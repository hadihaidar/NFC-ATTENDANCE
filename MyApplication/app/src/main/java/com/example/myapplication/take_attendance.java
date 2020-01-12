package com.example.myapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.example.myapplication.ui.Activity2;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class take_attendance extends Activity {
    public ArrayList<String> rfids = new ArrayList<String>();
    public static String rfid;
    public  String[] array;
    private TextView mTextView; //ECP 2017-01-16
    static int y =0;
    private ArrayList<TakeAttendance> attended  = new ArrayList<TakeAttendance>();

    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };
   Student[] studlis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attandance);
        Button b = findViewById(R.id.b1);   //Done
        Button b2 = findViewById(R.id.button); //Details
        ProgressBar p = findViewById(R.id.progressWheel);


        //Spinner dropdown = findViewById(R.id.spinner3);
        StudentByCourse s = new StudentByCourse();
        s.courseid = 1234;

        Gson gson = new Gson();
        try {
            s.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {

            studlis = gson.fromJson(s.res, Student[].class);
            String[] names = new String[studlis.length];
            for (int i=0;i<studlis.length;i++){
                names[i]=(studlis[i].getFirst()+" "+ studlis[i].getLast());   //add first name and last name of each student in the array
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);
            //set the spinners adapter to the previously created one.
            //dropdown.setAdapter(adapter);
             array = names.clone();

        } catch (Exception e) {
           System.out.println(e.getMessage());
        }
        p.setMax(array.length);
//        Button b2 = findViewById(R.id.button2);
//        b2.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//
//                Gson gson = new Gson();
//                StudentByCourse s = new StudentByCourse();
//                s.courseid = 1234;
//                s.execute();
//                Student[] studlis = gson.fromJson(s.res, Student[].class);
//
//            }
//        });
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                y=0;
                for (TakeAttendance post: attended){
                    post.execute();
                }
                Intent myIntent = new Intent(take_attendance.this, Activity2.class);
                startActivity( myIntent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(take_attendance.this, getDetails.class);
                startActivity( myIntent);
            }
        });

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        fadeIn.setStartOffset(200);

        fadeIn.setRepeatMode(Animation.REVERSE);
        fadeIn.setRepeatCount(Animation.INFINITE);

        findViewById(R.id.textView7).setAnimation(fadeIn);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "1");

        //mTextView.setText("onResume:");
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("onPause", "1");

        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }
    public  ArrayList<String> names = new ArrayList<>();
    public ArrayList<String> attending = new ArrayList<>();
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("onNewIntent", "1");

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            Log.d("onNewIntent", "2");
            Parcelable pe = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Tag tagg = (Tag) pe;
            byte[] idd = tagg.getId();
            rfid = getHex(idd);

            ProgressBar p = findViewById(R.id.progressWheel);
            String message= "Scanned IDs exceed total number of students";
            String message2 = "ID has been already scanned";

            if(y<array.length && !rfids.contains(rfid)){
                rfids.add(rfid);
                y++;



                int length = studlis.length;
                int x = 0;
                for (int i=0;i<studlis.length;i++){
                    if(rfid.equals(studlis[i].getStudentRFID()) && !attending.contains(rfid)) {
                        attending.add(rfid);
                    }
                    else{
                        names.add (studlis[i].getFirst() + " " + studlis[i].getLast());   //add first name and last name of each student in the array
                        x++;

                    }
                }
                if (x==0){
                    names =  new ArrayList<>();
                    names.add("No Abscent Students");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);
                //set the spinners adapter to the previously created one.
                //Spinner dropdown = findViewById(R.id.spinner3);
               // dropdown.setAdapter(adapter);

            Toast.makeText(take_attendance.this, rfid, Toast.LENGTH_SHORT).show();
        }
           else if (y>=array.length && !rfids.contains(rfid)) {

                Toast.makeText(take_attendance.this, message, Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(take_attendance.this, message2, Toast.LENGTH_SHORT).show();
            }
            p.setProgress(y);
            TextView textView = (TextView)findViewById(R.id.textView333);
            textView.setText(Integer.toString(y));

            /////post request to the API
            TakeAttendance post = new TakeAttendance();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String Date = df.format(new Date());
            post.date = Date;
            post.rfid = rfid;
            post.courseid =1234;
            try{
            attended.add(post);
            }
            catch(Exception e ){

            }



            Parcelable tagN = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tagN != null) {
                Log.d(TAG, "Parcelable OK");
                NdefMessage[] msgs;
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                byte[] payload = dumpTagData(tagN).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };

            }
            else {
                Log.d(TAG, "Parcelable NULL");
            }

            Parcelable[] messages1 = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (messages1 != null) {
                Log.d(TAG, "Found " + messages1.length + " NDEF messages");
            }
            else {
                Log.d(TAG, "Not EXTRA_NDEF_MESSAGES");
            }

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            if(ndef != null) {

                Log.d("onNewIntent:", "NfcAdapter.EXTRA_TAG");

                Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (messages != null) {
                    Log.d(TAG, "Found " + messages.length + " NDEF messages");
                }
            }
            else {
                Log.d(TAG, "Write to an unformatted tag not implemented");
            }


            //mTextView.setText( "NFC Tag\n" + ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_TAG)));
        }
    }
    @Override
    public void onBackPressed() {
        y=0;
        super.onBackPressed();
        System.gc();

    }

    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");



        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }
        Log.d("Datos: ", sb.toString());


        return sb.toString();
    }


    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private String ByteArrayToHexString(byte [] inarray) {

        Log.d("ByteArrayToHexString", inarray.toString());

        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
//CE7AEED4
//EE7BEED4
        Log.d("ByteArrayToHexString", String.format("%0" + (inarray.length * 2) + "X", new BigInteger(1,inarray)));


        return out;
    }

}

class StudentByCourse extends AsyncTask<URL, Integer, Void> {
    public int courseid;
    public String res;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getStudents(int courseid ) throws Exception {

        String url = "https://nfcattend.azurewebsites.net/api/v1/getStudents?code=xG5z5bYRuSkn6XK6DXFlwzEaLXuwTll9wkkkgzCCR6PRTvH4aAaYlw==&course="+ courseid;
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
             res = getStudents(courseid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}



class TakeAttendance extends AsyncTask<Void, Integer, String> {
    public String rfid;
    public int courseid;
    public String date;

    private static String attend(String rfid , int courseid , String date) throws Exception {

        String url = "https://nfcattend.azurewebsites.net/api/v1/takeAttendance?code=8x9a8LV3u6lEorOuL4B6h/622uw7COQ4IkkZ0K8tFUacgAECrMFRHQ==";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("cache-control", "no-cache");
        con.addRequestProperty("accept-encoding", "gzip");
        String urlParameters = "rfid=" + rfid + "&courseid=" + Integer.toString(courseid) +"&date="+date;

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
        String result="";
        try {
            result = attend(rfid,courseid,date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
