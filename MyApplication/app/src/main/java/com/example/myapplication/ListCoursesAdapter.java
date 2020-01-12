package com.example.myapplication;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;




public class ListCoursesAdapter extends RecyclerView.Adapter<ListCoursesAdapter.MyViewHolder> {
    private Context mContext;
    private Integer[] mImage;
    private String[] mTitle;
    private String[] msubTitle;
    private String[] msubTitle2;
    private String[] msubTitle3;
    private String[] courseName;
    private int[] coursesId;
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

    public ListCoursesAdapter(Context mContext, Integer[] image, String[] title, String[] subTitle, String[] subTitle2, String[] subTitle3, int[] coursesId) {
        this.mContext = mContext;
        this.mImage = image;
        this.mTitle = title;
        this.msubTitle = subTitle;
        this.msubTitle2 = subTitle2;
        this.msubTitle3 = subTitle3;
        this.coursesId = coursesId;
        this.courseName = subTitle;
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
                //Change Here next activity
                Intent intent = new Intent(context, getDetails.class);
                //Passing values course Id and course Name to getDetails activity
                //Handle it in getDetails using: int courseId = getIntent().getIntExtra("courseId");
                //And String courseName = getIntent().getStringExtra("courseName");
                intent.putExtra("courseId", "" + coursesId[i]);

                intent.putExtra("courseName", courseName[i]);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mTitle.length;
    }
}