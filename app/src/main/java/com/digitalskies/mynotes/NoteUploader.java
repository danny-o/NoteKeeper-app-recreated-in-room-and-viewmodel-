package com.digitalskies.mynotes;

import android.app.job.JobParameters;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.digitalskies.mynotes.NoteKeeperProviderContract.Notes;

import java.util.ArrayList;


/**
 * Created by Jim.
 */

public class NoteUploader {
    private final String TAG = getClass().getSimpleName();

    private final Context mContext;
    private boolean mCanceled;
    public ArrayList<String> nn;

    public NoteUploader(Context context) {
        mContext = context;
    }

    public boolean isCanceled() {
        return mCanceled;
    }

    public void cancel() {
        mCanceled = true;
    }

    public void doUpload(Uri dataUri) {
        String[] columns = {
                Notes.COLUMN_COURSE_ID,
                Notes.COLUMN_NOTE_TITLE,
                Notes.COLUMN_NOTE_TEXT,
        };

        Cursor cursor = mContext.getContentResolver().query(dataUri, columns, null, null, null);
        int courseIdPos = cursor.getColumnIndex(Notes.COLUMN_COURSE_ID);
        int noteTitlePos = cursor.getColumnIndex(Notes.COLUMN_NOTE_TITLE);
        int noteTextPos = cursor.getColumnIndex(Notes.COLUMN_NOTE_TEXT);

        Log.i(TAG, ">>>*** UPLOAD START - " + dataUri + " ***<<<");
        mCanceled = false;
        while(!mCanceled && cursor.moveToNext()) {
            String courseId = cursor.getString(courseIdPos);
            String noteTitle = cursor.getString(noteTitlePos);
            String noteText = cursor.getString(noteTextPos);

            if(!noteTitle.equals("")) {
                Log.i(TAG, ">>>Uploading Note<<< " + courseId + "|" + noteTitle + "|" + noteText);
                simulateLongRunningWork(4);
            }
        }
        if(mCanceled)
            Log.i(TAG, ">>>*** UPLOAD !!CANCELED!! - " + dataUri + " ***<<<");
        else
            Log.i(TAG, ">>>*** UPLOAD COMPLETE - " + dataUri + " ***<<<");
        cursor.close();
    }

   public int simulateLongRunningWork(int a) {
        int t=a*5;
       nn = new ArrayList<>();


        try {
            Thread.sleep(3000);
            addList("name","age");

        } catch(Exception ex) {}
        return t;
    }

    private void addList(String name, String age) {
        nn.add(name);
        nn.add(age);
    }

}
