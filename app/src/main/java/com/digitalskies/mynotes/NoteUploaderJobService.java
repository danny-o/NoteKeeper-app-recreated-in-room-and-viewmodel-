package com.digitalskies.mynotes;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.Uri;
import android.os.AsyncTask;

public class NoteUploaderJobService extends JobService {

    private NoteUploader mNoteUploader;

    public NoteUploaderJobService() {
    }
    public static String EXTRA_DATA_URI="com.digitalskies.mynotes.extras.DATA_URI";

    @Override
    public boolean onStartJob(final JobParameters params) {
        mNoteUploader = new NoteUploader(this);
        //onStart job receives work on main thread. Work has to be transferred to another thread for
        //proper performance
        @SuppressLint("StaticFieldLeak") AsyncTask<JobParameters,Void,Void> task=new AsyncTask<JobParameters, Void, Void>() {
            @Override
            protected Void doInBackground(JobParameters... backgroundParams) {
                JobParameters parameters=backgroundParams[0];
                String stringDataUri=parameters.getExtras().getString(EXTRA_DATA_URI);
                Uri dataUri= Uri.parse(stringDataUri);
                mNoteUploader.doUpload(dataUri);
                if(!mNoteUploader.isCanceled())
                    jobFinished(parameters,false);


                return null;
            }
        };
        task.execute(params);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mNoteUploader.cancel();
        return true;
    }


}
