package com.digitalskies.mynotes.data;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {EntityNoteInfo.class,EntityCourseInfo.class},version = 1)
public abstract class RoomDb extends RoomDatabase {
    static  final String DATABASE_NAME="notes_database";
    private static RoomDb INSTANCE;
    public abstract RoomDao getRoomDao();
    private static ExecutorService threadPool= Executors.newFixedThreadPool(3);
    private static RoomDatabase.Callback roomDbCallBack=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            RoomDao dao=INSTANCE.getRoomDao();
             NotesCoursesInitializer notesCoursesInitializer=new NotesCoursesInitializer(dao);
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    notesCoursesInitializer.insertCourses();

                }
            });
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    notesCoursesInitializer.insertSampleNotes();
                }
            });
        }
    };

    public static RoomDb getDatabase(Context context){
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),RoomDb.class,DATABASE_NAME)
                    .addCallback(roomDbCallBack)
                    .build();
        }
        return INSTANCE;
    }
}
