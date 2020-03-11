package com.digitalskies.mynotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.digitalskies.mynotes.data.EntityCourseInfo;
import com.digitalskies.mynotes.data.EntityNoteInfo;
import com.digitalskies.mynotes.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class IndividualNoteActivity extends AppCompatActivity
 {
    public static final String NOTE_ID = "com.digitalskies.mynotes.Id";
    public static final Long ID_NOT_SET = (long) -1;
    private Spinner spinnerCourses;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private boolean isCancelling=false;
    private Long mNoteId;
    private Uri mNoteUri;
    private String courseIdSendBroadcast;
    private ArrayAdapter<String> arrayAdapter;
     private List<String> courseTitles=new ArrayList<>();
     private NoteViewModel noteViewModel;

     Observer<List<EntityCourseInfo>> courseObserver=new Observer<List<EntityCourseInfo>>() {
         @Override
         public void onChanged(List<EntityCourseInfo> entityCourseInfo) {
             getCourseTitle(entityCourseInfo);
         }
     };
     Observer<EntityNoteInfo> noteObserver=new Observer<EntityNoteInfo>() {
         @Override
         public void onChanged(EntityNoteInfo entityNoteInfo) {
             setNoteDetails(entityNoteInfo);
         }
     };



     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         spinnerCourses = findViewById(R.id.spinner_courses);
        textNoteTitle = findViewById(R.id.text_note_title);
        textNoteText = findViewById(R.id.text_note_text_text);

         noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
         mNoteId=getIntent().getLongExtra(NOTE_ID,ID_NOT_SET);


         noteViewModel.getCourses().observe(this,courseObserver);
        noteViewModel.getNote(mNoteId);
         noteViewModel.noteInfo.observe(this,noteObserver);

    }
    public  void getCourseTitle(List<EntityCourseInfo> courseList){
        courseTitles= noteViewModel.getCoursesTitles(courseList);
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, courseTitles);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(arrayAdapter);


    }
     private void setNoteDetails(EntityNoteInfo entityNoteInfo) {

         noteViewModel.setNote(entityNoteInfo);
         displayNote();
     }



     private void displayNote() {

        int index=noteViewModel.getIndexToSet();
        spinnerCourses.setSelection(index);
        textNoteTitle.setText(noteViewModel.getNoteTitle());

        textNoteText.setText(noteViewModel.getNoteText());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("INDIVIDUAL NOTE:","...onPause running....");
        if(!isCancelling){
           noteViewModel.saveNote(textNoteTitle.getText().toString(),textNoteText.getText().toString()
           ,spinnerCourses.getSelectedItemPosition());
        }
        else if(isCancelling&&mNoteId==ID_NOT_SET){
            noteViewModel.deleteNote();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete) {
            deleteNote();
            finish();
            return true;
        }
        else if(id==R.id.action_cancel){
            isCancelling = true;
            finish();
        }
        else if(id==R.id.action_set_reminder){
            showReminderNotification();
            return  true;

        }
        else if(id==R.id.send_broadcast){
            CourseEventBroadcastHelper.sendEventBroadcast(this,courseIdSendBroadcast,"Editing Note");
        }
        return super.onOptionsItemSelected(item);
    }

     private void deleteNote() {
         noteViewModel.deleteNote();
     }

     private void showReminderNotification() {
        String noteTitle=textNoteTitle.getText().toString();
        String noteText=textNoteText.getText().toString();
        int noteId=(int)ContentUris.parseId(mNoteUri);
        Intent intent=new Intent(this,NoteReceiver.class);
        intent.putExtra(NoteReceiver.EXTRA_NOTE_TITLE,noteTitle);
        intent.putExtra(NoteReceiver.EXTRA_NOTE_TEXT,noteText);
        intent.putExtra(NoteReceiver.EXTRA_NOTE_ID,noteId);

        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

        long currentTimeInMilliSecs= SystemClock.elapsedRealtime();
        long TWENTY_SECONDS=20*1000;
        long alarmTime=currentTimeInMilliSecs+TWENTY_SECONDS;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,alarmTime,pendingIntent);

    }

    }


