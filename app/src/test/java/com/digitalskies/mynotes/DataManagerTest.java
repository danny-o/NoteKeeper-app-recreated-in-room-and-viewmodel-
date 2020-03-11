package com.digitalskies.mynotes;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {
    static  DataManager sDataManager;

    //this method runs before all methods are run i.e will run once before any tests begin including before the @Before methods
    //@BeforeClass methods and all the members they set have to be static
    @BeforeClass
    public static void classSetUp() throws Exception{
        sDataManager=DataManager.getInstance();
    }
    //this method runs before each method is run i.e for each test method that is run it has to run before that test
    @Before
    public void setUp() throws Exception{
        sDataManager.getNotes().clear();
        sDataManager.initializeExampleNotes();
    }

    @Test
    public void createNewNote()throws Exception {
        final  CourseInfo course= sDataManager.getCourse("android_async");
        final String noteTitle="Test note title";
        final String noteText="This is the body text of my test note";

        int noteIndex=sDataManager.createNewNote();
        NoteInfo newNote=sDataManager.getNotes().get(noteIndex);
        newNote.setCourse(course);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);

        NoteInfo compareNote=sDataManager.getNotes().get(noteIndex);
        assertEquals(course,compareNote.getCourse());
        assertEquals(noteTitle,compareNote.getTitle());
        assertEquals(noteText,compareNote.getText());

    }
   @Test
    public void findSimilarNotes() throws Exception{
        final CourseInfo course=sDataManager.getCourse("android_async");
        final String noteTitle="Test note title";
        final String noteText1="This is the body text of my test note";
       final String noteText2="This is the body text of my second test note";

       int noteIndex1=sDataManager.createNewNote();
       NoteInfo newNote1=sDataManager.getNotes().get(noteIndex1);
       newNote1.setCourse(course);
       newNote1.setTitle(noteTitle);
       newNote1.setText(noteText1);

       int noteIndex2=sDataManager.createNewNote();
       NoteInfo newNote2=sDataManager.getNotes().get(noteIndex2);
       newNote2.setCourse(course);
       newNote2.setTitle(noteTitle);
       newNote2.setText(noteText2);

       int foundIndex1=sDataManager.findNote(newNote1);
       assertEquals(noteIndex1,foundIndex1);

       int foundIndex2=sDataManager.findNote(newNote2);
       assertEquals(noteIndex2,foundIndex2);



   }
   @Test
    public void createNewNoteOneStepCreation(){
       final CourseInfo course=sDataManager.getCourse("android_async");
       final String noteTitle="Test note title";
       final String noteText1="This is the body my test note";
       int noteIndex=sDataManager.createNewNote(course,noteTitle,noteText1);

       NoteInfo compareNote=sDataManager.getNotes().get(noteIndex);

       assertEquals(course,compareNote.getCourse());
       assertEquals(noteTitle,compareNote.getTitle());
       assertEquals(noteText1,compareNote.getText());

   }

}