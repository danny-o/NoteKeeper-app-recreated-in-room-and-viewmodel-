package com.digitalskies.mynotes.main;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalskies.mynotes.BuildConfig;
import com.digitalskies.mynotes.CourseRecyclerAdapter;
import com.digitalskies.mynotes.IndividualNoteActivity;
import com.digitalskies.mynotes.NoteBackup;
import com.digitalskies.mynotes.NoteBackupService;
import com.digitalskies.mynotes.NoteKeeperProviderContract.Notes;
import com.digitalskies.mynotes.NoteRecyclerAdapter;
import com.digitalskies.mynotes.NoteUploaderJobService;
import com.digitalskies.mynotes.R;
import com.digitalskies.mynotes.SettingsActivity;
import com.digitalskies.mynotes.data.EntityCourseInfo;
import com.digitalskies.mynotes.data.NoteDetails;
import com.digitalskies.mynotes.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener     {
    private static final int NOTE_UPLOADER_JOB_ID = 1;
    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager notesLayoutManager;
    private GridLayoutManager courseLayoutManager;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private NavigationHandler navigationHandler;
    private List<EntityCourseInfo> courses=new ArrayList<>();
    NoteViewModel noteViewModel;
    Observer<List<NoteDetails>> noteDetailsObserver=new Observer<List<NoteDetails>>() {
        @Override
        public void onChanged(List<NoteDetails> noteDetails) {
            updateData(noteDetails);
        }
    };
    private List<NoteDetails> noteItems=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enableStrictMode();
        mRecyclerItems = findViewById(R.id.list_items);
        notesLayoutManager = new LinearLayoutManager(this);
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this);
        mNoteRecyclerAdapter.setItems(noteItems);
        mRecyclerItems.setLayoutManager(notesLayoutManager);
        mRecyclerItems.setAdapter(mNoteRecyclerAdapter);
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, courses);
        courseLayoutManager = new GridLayoutManager(this,getResources().getInteger(R.integer.course_grid_span));

        noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getNoteDetails().observe(this,noteDetailsObserver);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, IndividualNoteActivity.class)));
        androidx.preference.PreferenceManager.setDefaultValues(this,R.xml.root_preferences,true);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationHandler=new NavigationHandler(this);

    }
    private void enableStrictMode() {
        if(BuildConfig.DEBUG) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateNavHeader();
       navigationHandler.openDrawer();
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    private void updateNavHeader() {
        NavigationView nav=findViewById(R.id.nav_view);
        View headerView=nav.getHeaderView(0);
        TextView textUserName=headerView.findViewById(R.id.text_user_name);
        TextView textEmailAddress=headerView.findViewById(R.id.text_email_address);

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        String userName=pref.getString("display_name","Daniel");
        String userEmailAddress=pref.getString("user_email_address","");
        textUserName.setText(userName);
        textEmailAddress.setText(userEmailAddress);
    }
    private void updateData(List<NoteDetails> noteDetails){

            mNoteRecyclerAdapter.setItems(noteDetails);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if(id==R.id.notes_backup){
            backUpNotes();
            return true;
        }
        if(id==R.id.action_upload_notes){
            scheduleNoteUpload();
        }

        return super.onOptionsItemSelected(item);
    }

    private void scheduleNoteUpload() {
        PersistableBundle extras=new PersistableBundle();
        extras.putString(NoteUploaderJobService.EXTRA_DATA_URI,Notes.CONTENT_URI.toString());
        ComponentName componentName=new ComponentName(this,NoteUploaderJobService.class);
        JobInfo jobInfo=new JobInfo.Builder(NOTE_UPLOADER_JOB_ID,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setExtras(extras)
                .build();
        JobScheduler jobScheduler=(JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);

    }

    private void backUpNotes() {
        Intent intent=new Intent(this, NoteBackupService.class);
        intent.putExtra(NoteBackupService.EXTRA_COURSE_ID, NoteBackup.ALL_COURSES);
        startService(intent);


    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            navigationHandler.displayNotes();
        } else if (id == R.id.nav_courses) {

            navigationHandler.displayCourses();
        }  else if (id == R.id.nav_share) {
            navigationHandler.handleShare(R.string.notes);
        } else if (id == R.id.nav_send) {
            navigationHandler.handleSelection();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
