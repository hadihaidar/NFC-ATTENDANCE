package com.example.myapplication.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Course;
import com.example.myapplication.ListCoursesAdapter;
import com.example.myapplication.R;
import com.example.myapplication.ui.Activity2;
import com.example.myapplication.ui.adapter;
import com.google.gson.Gson;


public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    Integer[] drawableArray;
    String[] titleArray;
    String[] subtitleArray;
    String[] subtitleArray2;
    String[] subtitleArray3;
    int[] ids;
    ListCoursesAdapter ad;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        shareViewModel = ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        final RecyclerView rv = root.findViewById(R.id.relative);

        Integer[] drawableArray = {R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo,R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo}; //To be replaced by Select Icons

        String json = Activity2.json;
        Course[] courseslis;

        Gson gson = new Gson();
        courseslis = gson.fromJson(json, Course[].class); //Res refers to result
        drawableArray = new Integer[courseslis.length];
        titleArray = new String[courseslis.length];
        subtitleArray = new String[courseslis.length];
        subtitleArray2 = new String[courseslis.length];
        subtitleArray3 = new String[courseslis.length];
         ids = new int[courseslis.length];

        for (int i=0; i<courseslis.length; i++){
            drawableArray[i] = R.drawable.logo;
            titleArray[i] = courseslis[i].getSubject();
            subtitleArray[i] = courseslis[i].getTitle();
            subtitleArray2[i] = courseslis[i].getDays() + " " + courseslis[i].getTime();
            subtitleArray3[i] = courseslis[i].getBuilding() + " " +courseslis[i].getRoom();
            ids[i] = courseslis[i].getCourseId();
        }
        adapter ad;
        super.onCreate(savedInstanceState);
        ad = new adapter(getActivity(),drawableArray,titleArray,subtitleArray,subtitleArray2,subtitleArray3,ids);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        rv.setAdapter(ad);
        Activity2.fab.hide();

        return root;
    }
}

