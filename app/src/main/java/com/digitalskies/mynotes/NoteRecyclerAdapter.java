package com.digitalskies.mynotes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalskies.mynotes.data.NoteDetails;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private Cursor mCursor;
    private final LayoutInflater layoutInflater;
    private int mCoursePos;
    private int mNoteTitle;
    private int mIdPos;
    NoteDetails noteDetails;
    List<NoteDetails> notesList;

    public NoteRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        noteDetails=new NoteDetails();
        layoutInflater = LayoutInflater.from(mContext);
    }
    public void setItems(List<NoteDetails> noteDetails){
        notesList=noteDetails;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NoteRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //The parent is the viewGroup that is being inflated within
        //The guess is that it is the frameLayout within which cardView is contained
        //The boolean indicates if the newly inflated view to be automatically attached to its parent
        //The itemview points to the root of the view created when item_note_list is inflated
        //in this case the cardView and the textViews
            View itemView=layoutInflater.inflate(R.layout.item_note_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteRecyclerAdapter.ViewHolder holder, int position) {


       noteDetails=notesList.get(position);
        String course=noteDetails.getCourseTitle();
        String noteTitle=noteDetails.getNoteTitle();


        holder.textCourse.setText(course);
        holder.textTitle.setText(noteTitle);
        holder.mId =noteDetails.getId();
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textCourse;
        public final TextView textTitle;
        public  Long mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCourse = itemView.findViewById(R.id.text_course);
            textTitle = itemView.findViewById(R.id.text_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,IndividualNoteActivity.class);
                    intent.putExtra(IndividualNoteActivity.NOTE_ID, mId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
