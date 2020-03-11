package com.digitalskies.mynotes.main;

import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.digitalskies.mynotes.R;
import com.digitalskies.mynotes.main.NavigationHandlerInterface;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class NavigationHandler implements NavigationHandlerInterface {
    private AppCompatActivity activity;
    private NavigationView nav;
    NavigationHandler(AppCompatActivity activity){
        this.activity=activity;
        nav=activity.findViewById(R.id.nav_view) ;
        displayNotes();
    }

    @Override
    public void openDrawer() {
        Handler handler=new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DrawerLayout drawer=activity.findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        },1000);
    }
    public void displayNotes() {

        selectNavigationMenuItem(R.id.nav_notes);

    }
    public void displayCourses() {

        selectNavigationMenuItem(R.id.nav_courses);
    }

    @Override
    public void selectNavigationMenuItem(int id) {

        Menu menu=nav.getMenu();
        menu.findItem(id).setChecked(true);
    }

    @Override
    public void handleShare(int message_id) {
        View view=activity.findViewById(R.id.list_items);
        Snackbar.make(view,message_id,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void handleSelection() {

    }
}
