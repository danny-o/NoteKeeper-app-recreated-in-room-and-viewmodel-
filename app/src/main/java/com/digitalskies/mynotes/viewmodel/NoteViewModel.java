package com.digitalskies.mynotes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.digitalskies.mynotes.Note;
import com.digitalskies.mynotes.data.DataOperations;
import com.digitalskies.mynotes.data.EntityCourseInfo;
import com.digitalskies.mynotes.data.EntityNoteInfo;
import com.digitalskies.mynotes.data.NoteDetails;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    public MutableLiveData<EntityNoteInfo> noteInfo=new MutableLiveData<>();
    private DataOperations dataOperations;
    private Note note;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        dataOperations =new DataOperations(application);
        note=new Note(dataOperations);

    }
    public LiveData<List<NoteDetails>> getNoteDetails() {
        return dataOperations.getNotesDetails();
    }
    public void saveNote(String Title, String Text, int itemSelected) {
        note.saveNote(Title,Text,itemSelected);
    }

    public void deleteNote(){
        note.deleteNote();
    }
    public void setNote(EntityNoteInfo entityNoteInfo) {
        note.setNote(entityNoteInfo);
    }

    public void getNote(Long noteId) {
        int ID_NOT_SET = -1;
        noteInfo= dataOperations.getNoteInfo();
        if(noteId== ID_NOT_SET){
                note.createNewNote();
        }
        else{

           dataOperations.getNote(noteId);
        }


    }
    public LiveData<List<EntityCourseInfo>> getCourses(){

        return dataOperations.getAllCourses();
    }

    public List<String> getCoursesTitles(List<EntityCourseInfo> courseList) {
       return note.getCoursesTitles(courseList);
    }
    public  int getIndexToSet(){
        return  note.indexToSet();
    }
    public String getNoteTitle(){
        return note.getNoteTitle();
    }
    public String getNoteText(){
        return note.getNoteText();
    }

}
