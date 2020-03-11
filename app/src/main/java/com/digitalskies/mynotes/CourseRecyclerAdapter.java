package com.digitalskies.mynotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalskies.mynotes.data.EntityCourseInfo;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {
    private final List<EntityCourseInfo> mCourses;
    private final Context mContext;
    private final LayoutInflater layoutInflater;

    public CourseRecyclerAdapter(Context mContext, List<EntityCourseInfo> mCourses) {
        this.mCourses = mCourses;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public CourseRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //The parent is the viewGroup that is being inflated within
        //The guess is that it is the frameLayout within which cardView is contained
        //The boolean indicates if the newly inflated view to be automatically attached to its parent
        //The itemview points to the root of the view created when item_note_list is inflated
        //in this case the cardView and the textViews
            View itemView=layoutInflater.inflate(R.layout.item_course_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseRecyclerAdapter.ViewHolder holder, int position) {
        EntityCourseInfo course=mCourses.get(position);
        holder.textCourse.setText(course.getCourseTitle());
        holder.mCurrentPosition=position;
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textCourse;
        public  int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCourse = itemView.findViewById(R.id.text_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v,mCourses.get(mCurrentPosition).getCourseTitle(),
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
