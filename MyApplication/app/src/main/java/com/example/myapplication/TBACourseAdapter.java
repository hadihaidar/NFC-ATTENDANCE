package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.Activity2;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class TBACourseAdapter extends RecyclerView.Adapter<TBACourseAdapter.MyViewHolder> {
    private Context mContext;
    private Integer[] mImage;
    private String[] mTitle;
    private String[] msubTitle;
    private String[] msubTitle2;
    private String[] msubTitle3;
    private String instructorId;
    private Integer[] courseId;
    private String name ;
    private String aubnet;
    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView subtitle;
        TextView subtitle2;
        TextView subtitle3;
        ImageView imgView;

        public MyViewHolder(View itemView) {
            super(itemView);


            this.title = (TextView) itemView.findViewById(R.id.title);
            this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            this.subtitle2 = (TextView) itemView.findViewById(R.id.subtitle2);
            this.subtitle3 = (TextView) itemView.findViewById(R.id.subtitle3);
            this.imgView = (ImageView) itemView.findViewById(R.id.imgcar);
        }
    }

    public TBACourseAdapter(Context mContext, Integer[] image, String[] title, String[] subTitle,String[] subTitle2, String[] subTitle3, String instructorId, Integer[] courseId, String name, String aubnet) {
        this.mContext = mContext;
        this.mImage = image;
        this.mTitle = title;
        this.msubTitle = subTitle;
        this.msubTitle2 = subTitle2;
        this.msubTitle3 = subTitle3;
        this.instructorId = instructorId;
        this.courseId = courseId;
        this.name = name;
        this.aubnet=aubnet;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                           final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int i) {
        holder.title.setText(mTitle[i]);
        holder.subtitle.setText(msubTitle[i]);
        holder.subtitle2.setText(msubTitle2[i]);
        holder.subtitle3.setText(msubTitle3[i]);
        Picasso.with(mContext).load(mImage[i]).into(holder.imgView);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, Activity2.class);

                //Call http Post Request from Here
                //pass to it courseId[i]; and instructorId
                //Query UPDATE dbo.Courses SET instructorId = -1 WHERE courseId = 4;


                ChangeInstructorID change = new ChangeInstructorID();
                change.courseId = courseId[i];
                change.newInstructorId = instructorId;
                change.name = name;

                try {
                    change.execute().get();
                }
                catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                intent.putExtra("instructorID", instructorId );

                intent.putExtra("name", name );

                intent.putExtra("aubnet", aubnet );



                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return mTitle.length;
    }
}
