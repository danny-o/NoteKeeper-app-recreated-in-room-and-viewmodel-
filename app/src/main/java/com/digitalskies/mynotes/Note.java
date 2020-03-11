package com.digitalskies.mynotes;

import com.digitalskies.mynotes.data.DataOperations;
import com.digitalskies.mynotes.data.EntityCourseInfo;
import com.digitalskies.mynotes.data.EntityNoteInfo;

import java.util.List;

public class Note {
    private EntityNoteInfo entityNoteInfo;
    private Courses courses;
    private DataOperations dataOperations;

    public Note(DataOperations dataOperations){
        this.dataOperations=dataOperations;
        courses = new Courses();
    }

    public void setNote(EntityNoteInfo note) {
           entityNoteInfo=note;
    }
    public  EntityNoteInfo getNote(){
        return  entityNoteInfo;
    }


    public String getCourseId() {
        return entityNoteInfo.getCourseId();
    }

    public String getNoteTitle() {
        return entityNoteInfo.getNoteTitle();
    }

    public String getNoteText() {
        return entityNoteInfo.getNoteText();
    }

    public void createNewNote(){
      entityNoteInfo=new EntityNoteInfo();
      entityNoteInfo.setCourseId("");
      entityNoteInfo.setNoteTitle("");
      entityNoteInfo.setNoteText("");
      dataOperations.insertNote(entityNoteInfo);
    }
    public int indexToSet(){

        return  courses.getIndexToSet(entityNoteInfo.getCourseId());
    }


    public List<String> getCoursesTitles(List<EntityCourseInfo> courseList) {
        return courses.getCourseTitles(courseList);
    }
    public void saveNote(String title,String text, int itemSelectedPosition) {
        if(title.equals("")&&text.equals("")){
            deleteNote();
        }
        else{
            entityNoteInfo.setNoteTitle(title);
            entityNoteInfo.setNoteText(text);
            entityNoteInfo.setCourseId(courses.getCourseId(itemSelectedPosition));

            dataOperations.updateNote(entityNoteInfo);
        }

    }

    public void deleteNote() {
        dataOperations.deleteNote(entityNoteInfo);
    }
}
