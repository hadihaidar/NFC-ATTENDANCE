package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.AddStudent;
import com.example.myapplication.getDetails;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import com.example.myapplication.take_attendance;

import androidx.recyclerview.widget.RecyclerView;

public class adapter extends RecyclerView.Adapter<adapter.viewholder> {
    private Context mContext;
    private Integer[] mImage;
    private String[] mTitle;
    private String[] msubTitle;
    private String[] msubTitle2;
    private String[] msubTitle3;
    private int[] ids;
    public static class viewholder extends RecyclerView.ViewHolder {

        TextView title;
        TextView subtitle;
        TextView subtitle2;
        TextView subtitle3;
        ImageView imgView;

        public viewholder(View itemView) {
            super(itemView);


            this.title = (TextView) itemView.findViewById(R.id.title);
            this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            this.subtitle2 = (TextView) itemView.findViewById(R.id.subtitle2);
            this.subtitle3 = (TextView) itemView.findViewById(R.id.subtitle3);
            this.imgView = (ImageView) itemView.findViewById(R.id.imgcar);
        }
    }

    public adapter(Context mContext, Integer[] image, String[] title, String[] subTitle, String[] subTitle2, String[] subTitle3, int[] ids) {
        this.mContext = mContext;
        this.mImage = image;
        this.mTitle = title;
        this.msubTitle = subTitle;
        this.msubTitle2 = subTitle2;
        this.msubTitle3 = subTitle3;
        this.ids = ids;
    }

    @Override
    public viewholder onCreateViewHolder(final ViewGroup parent,
                                           final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        viewholder myViewHolder = new viewholder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final viewholder holder, final int i) {
        holder.title.setText(mTitle[i]);
        holder.subtitle.setText(msubTitle[i]);
        holder.subtitle2.setText(msubTitle2[i]);
        holder.subtitle3.setText(msubTitle3[i]);
        Picasso.with(mContext).load(mImage[i]).into(holder.imgView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, AddStudent.class);
                intent.putExtra("courseID" , ""+ids[i]);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTitle.length;
    }
}

