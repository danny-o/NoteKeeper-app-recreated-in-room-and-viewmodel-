package com.digitalskies.mynotes.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataOperations {
    private RoomDb roomDb;
    private ExecutorService threadPool=Executors.newCachedThreadPool();
    private LiveData<List<NoteDetails>> noteDetails;
    private MutableLiveData<EntityNoteInfo> entityNoteInfo=new MutableLiveData<>();
    private MutableLiveData<List<EntityCourseInfo>> courseInfo=new MutableLiveData<>();
    private MutableLiveData<EntityNoteInfo> newNote=new MutableLiveData<>();

    public DataOperations(Context appContext){
        roomDb=RoomDb.getDatabase(appContext);
        noteDetails=roomDb.getRoomDao().getNotesDetails();

    }

    public  void insertNote(EntityNoteInfo note){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                roomDb.getRoomDao().insertNote(note);
                entityNoteInfo.postValue(roomDb.getRoomDao().getNoteByNoteTitle(""));
            }
        });

    }
    public MutableLiveData<EntityNoteInfo> getNoteInfo(){
        return  entityNoteInfo;
    }
    public void  deleteNote(EntityNoteInfo note){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                roomDb.getRoomDao().deleteNote(note);

            }
        });
    }
    public void updateNote(EntityNoteInfo note){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                roomDb.getRoomDao().updateNote(note);
            }
        });
    }


    public MutableLiveData<List<EntityCourseInfo>> getAllCourses(){

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                courseInfo.postValue(roomDb.getRoomDao().getCourses());
            }
        });
        return courseInfo;
    }
    public void getNote(Long noteId){

        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                entityNoteInfo.postValue(roomDb.getRoomDao().getNoteById(noteId));

            }
        });

    }
    public LiveData<List<NoteDetails>> getNotesDetails() {


        return noteDetails;
    }
}
