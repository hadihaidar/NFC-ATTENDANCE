package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class getDetails extends Activity {
    static int y = 0;
    private ArrayList<Attendance> attended = new ArrayList<Attendance>();
    ArrayList<String> missing = new ArrayList<>();
    ArrayList<String> attending = new ArrayList<>();
    ArrayList<String> rfids = new ArrayList<>();
    ArrayList<String> missingRfid = new ArrayList<>();
    ArrayList<String> AttendingRfid = new ArrayList<>();
    ArrayList<Student> AllStudent = new ArrayList<>();
    ArrayList<Student> attend = new ArrayList<>(); //NEW ARRAY
    ArrayList<CourseTime> times = new ArrayList<>();
    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        //getting the data from the api
        students s = new students();
        Intent intent = getIntent();
        String cID  = intent.getStringExtra("courseId");
        String cName = intent.getStringExtra("courseName");
        s.courseid = Integer.parseInt(cID);

        session session = new session();
        session.courseid = Integer.parseInt(cID);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        session.date = df.format(new Date());
        session.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        session.execute();

        Gson gson = new Gson();
        try {
            s.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Student[] studlis;
            studlis = gson.fromJson(s.res, Student[].class);
            for (int i = 0; i < studlis.length; i++) {
                AllStudent.add(studlis[i]);
                missing.add(studlis[i].getFirst() + " " + studlis[i].getLast());
                rfids.add(studlis[i].getStudentRFID());
                missingRfid.add(studlis[i].getStudentRFID());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //filling the lists with data
        final ListView list1 = (ListView) findViewById(R.id.list1);
        final ListView list2 = (ListView) findViewById(R.id.list2);
        TBACustomAdapter a = new TBACustomAdapter(getDetails.this, 8,times );
        list1.setAdapter(a);


        ArrayAdapter<String> present = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, attending);
        ArrayAdapter<String> abscent = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, missing){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position % 2 == 1) {
                    view.setBackgroundColor(Color.parseColor("#E94C3D"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#474747"));
                }

                return view;
            }
        };
        TextView t1 = findViewById(R.id.textView9);
        TextView t2 = findViewById(R.id.textView10);
        t1.setText("Attending Students: "+attending.size());
        t2.setText("Absent Students :"+missing.size());
       // list1.setAdapter(present);
        list2.setAdapter(abscent);
        //animation
        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(500);
        fadeIn.setStartOffset(100);
        fadeIn.setRepeatMode(Animation.REVERSE);
        fadeIn.setRepeatCount(Animation.INFINITE);
        findViewById(R.id.imageView2).setAnimation(fadeIn);

        final String jsonStuds = gson.toJson(AllStudent); //SEND ARRAYLIST THROUGH INTENT
        //final String courseTimes = gson.toJson(times); //SEND ARRAYLIST THROUGH INTENT

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(),"hi"+ position,Toast.LENGTH_SHORT).show();      //WORKS
                Intent intent = getIntent();
                String cID  = intent.getStringExtra("courseId");
                CourseTime ob = (CourseTime) list1.getItemAtPosition(position);   //position of the item
                Intent myIntent = new Intent(getDetails.this, getInfo.class);
                myIntent.putExtra("Full Name",ob.getFullname());
                myIntent.putExtra("RFID",AttendingRfid.get(position));
                myIntent.putExtra("array",jsonStuds);  //send arrayList of students
                myIntent.putExtra("courseID",cID);
                startActivity(myIntent);
        }
        });


        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(),"hello"+missingRfid.get(position),Toast.LENGTH_SHORT).show();    //WORKS
                Intent intent = getIntent();
                String cID  = intent.getStringExtra("courseId");
                String itemValue = (String) list2.getItemAtPosition(position);   //position of the item
                Intent myIntent = new Intent(getDetails.this, getInfo.class);
                myIntent.putExtra("Full Name", itemValue);  //send name to next activity
                myIntent.putExtra("RFID",missingRfid.get(position));
                myIntent.putExtra("array",jsonStuds);  //send arrayList of students
                myIntent.putExtra("courseID",cID);
                startActivity(myIntent);
            }
    });
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("onNewIntent", "1");

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            Log.d("onNewIntent", "2");
            Parcelable pe = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Tag tagg = (Tag) pe;
            byte[] idd = tagg.getId();
            String rfid = getHex(idd);

            ProgressBar p = findViewById(R.id.progressWheel);
            String message = "Scanned IDs exceed total number of students";
            String message2 = "ID has been already scanned";
            ///check if student is registered in  the course
            if (!rfids.contains(rfid))
                Toast.makeText(getDetails.this, "Student Not Registered", Toast.LENGTH_SHORT).show();

            else {
                if (!AttendingRfid.contains(rfid)) {//check if he already scanned before
                    AttendingRfid.add(rfid);
                    missingRfid.remove(rfid);
                    for (Student s : AllStudent) {
                        if (s.getStudentRFID().equals(rfid)) {
                            Attendance a = new Attendance();
                            a.rfid=rfid;
                           a.courseid = s.getCourseId();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String Date = df.format(new Date());
                            a.date = Date;
                            attended.add(a);
                            a.execute();
                            String full = s.getFirst() + " " + s.getLast();
                            times.add(new CourseTime(full, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));

                            attending.add(s.getFirst() + " " + s.getLast() +"\t" + LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                            missing.remove(s.getFirst() + " " + s.getLast());
                            // Toast.makeText(getDetails.this, s.getFirst() + " " + s.getLast(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    ListView list1 = (ListView) findViewById(R.id.list1);
                    ListView list2 = (ListView) findViewById(R.id.list2);
                    times = arrayListReverse(times);
                    TBACustomAdapter a = new TBACustomAdapter(getDetails.this, 0,times );
                    list1.setAdapter(a);

                    Collections.reverse(attending);

                    ArrayAdapter<String> abscent = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, missing){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.WHITE);
                            if (position % 2 == 1) {
                                view.setBackgroundColor(Color.parseColor("#E94C3D"));
                            } else {
                                view.setBackgroundColor(Color.parseColor("#474747"));
                            }

                            return view;
                        }
                    };
                    TextView t1 = findViewById(R.id.textView9);
                    TextView t2 = findViewById(R.id.textView10);
                    t1.setText("Attending Students: "+attending.size());
                    t2.setText("Absent Students: "+missing.size());
                    ArrayAdapter<String> present = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, attending);
                    //list1.setAdapter(present);
                    list2.setAdapter(abscent);

                } else {
                    Toast.makeText(getDetails.this, message2, Toast.LENGTH_SHORT).show();
                }
            }
        }


        Parcelable tagN = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tagN != null) {
            Log.d(TAG, "Parcelable OK");
            NdefMessage[] msgs;
            byte[] empty = new byte[0];
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            byte[] payload = dumpTagData(tagN).getBytes();
            NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
            NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
            msgs = new NdefMessage[]{msg};

        } else {
            Log.d(TAG, "Parcelable NULL");
        }

        Parcelable[] messages1 = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (messages1 != null) {
            Log.d(TAG, "Found " + messages1.length + " NDEF messages");
        } else {
            Log.d(TAG, "Not EXTRA_NDEF_MESSAGES");
        }

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);
        if (ndef != null) {

            Log.d("onNewIntent:", "NfcAdapter.EXTRA_TAG");

            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (messages != null) {
                Log.d(TAG, "Found " + messages.length + " NDEF messages");
            }
        } else {
            Log.d(TAG, "Write to an unformatted tag not implemented");
        }


        //mTextView.setText( "NFC Tag\n" + ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_TAG)));
    }
    public static <T> ArrayList<T> arrayListReverse(ArrayList<T> lst) {
        ArrayList<T> reversed= new ArrayList<T>();
        for (int i=lst.size()-1;i>=0;i--){
            T t = lst.get(i);
            reversed.add(t);
        }
        return reversed;
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

    @Override
    public void onBackPressed() {
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

    private String ByteArrayToHexString(byte[] inarray) {

        Log.d("ByteArrayToHexString", inarray.toString());

        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
//CE7AEED4
//EE7BEED4
        Log.d("ByteArrayToHexString", String.format("%0" + (inarray.length * 2) + "X", new BigInteger(1, inarray)));


        return out;
    }

}


class students extends AsyncTask<URL, Integer, Void> {
        public int courseid;
        public String res;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private static String getStudents(int courseid) throws Exception {

            String url = "https://nfcattend.azurewebsites.net/api/v1/Students?code=xG5z5bYRuSkn6XK6DXFlwzEaLXuwTll9wkkkgzCCR6PRTvH4aAaYlw==&course=" + courseid;
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
                return (response.toString());

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

class Attendance extends AsyncTask<Void, Integer, String> {
    public String rfid;
    public int courseid;
    public String date;

    private static String attend(String rfid, int courseid, String date) throws Exception {

        String url = "https://nfcattend.azurewebsites.net/api/v1/MarkAttendance?code=71K4A8k7LldharBgVtcMVjEJMvpnvFR1rIjcaCmDpOhGQmOAtiUFyw==";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String urlParameters = "rfid=" + rfid + "&courseid=" + Integer.toString(courseid) + "&date=" + date;

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
            result = attend(rfid, courseid, date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}



class session extends AsyncTask<Void, Integer, String> {
    public int courseid;
    public String date;
    public String time;
    private static String attend(String time, int courseid, String date) throws Exception {

        String url = "https://nfcattend.azurewebsites.net/api/v1/NewSession?code=bF4EXla7gW6kXhifCqrGiOg51Y/xtRJOCCfTz3QHuK0L3HYgs6ODiw==";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("cache-control", "no-cache");
        con.addRequestProperty("accept-encoding", "gzip");
        String urlParameters = "courseid=" + Integer.toString(courseid) + "&date=" + date + "&start="+time+"&finish=6:00:00";

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
            result = attend(time, courseid, date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}


