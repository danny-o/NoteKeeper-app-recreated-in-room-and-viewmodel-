package com.digitalskies.mynotes;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;

import androidx.annotation.Nullable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NoteBackupService extends IntentService {
    public static final String EXTRA_COURSE_ID = "com.digitalskies.mynotes.extra.COURSE_ID";

    public NoteBackupService() {
        super(
                "NoteBackupService");
    }

    @Override

    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String backUpCourseId=intent.getStringExtra(EXTRA_COURSE_ID);
            NoteBackup.doBackup(this,backUpCourseId);

            }
        }

}



