package com.digitalskies.mynotes.data;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * Created by Jim.
 */

public class NotesCoursesInitializer {
    private EntityCourseInfo courseInfo;
    private EntityNoteInfo noteInfo;
    private RoomDao roomDao;

    private ArrayList<EntityCourseInfo> courseList = new ArrayList<>();
    private ArrayList<EntityNoteInfo> noteList = new ArrayList<>();


    public NotesCoursesInitializer(RoomDao roomDao) {
        this.roomDao = roomDao;

    }

    public void insertCourses() {
        insertCourse("android_intents", "Android Programming with Intents");

        insertCourse("android_async", "Android Async Programming and Services");

        insertCourse("java_lang", "Java Fundamentals: The Java Language");
        insertCourse("java_core", "Java Fundamentals: The Core Platform");


                for (int count = 0; count < courseList.size(); count++) {
                    EntityCourseInfo courseInfo;
                    courseInfo = courseList.get(count);
                    roomDao.insertCourseInfo(courseInfo);

                }




    }

    public void insertSampleNotes() {
        insertNote("android_intents", "Dynamic intent resolution", "Wow, intents allow components to be resolved at runtime");
        insertNote("android_intents", "Delegating intents", "PendingIntents are powerful; they delegate much more than just a component invocation");

        insertNote("android_async", "Service default threads", "Did you know that by default an Android Service will tie up the UI thread?");
        insertNote("android_async", "Long running operations", "Foreground Services can be tied to a notification icon");

        insertNote("java_lang", "Parameters", "Leverage variable-length parameter lists?");
        insertNote("java_lang", "Anonymous classes", "Anonymous classes simplify implementing one-use types");

        insertNote("java_core", "Compiler options", "The -jar option isn't compatible with with the -cp option");
        insertNote("java_core", "Serialization", "Remember to include SerialVersionUID to assure version compatibility");

                for (int count = 0; count < noteList.size(); count++) {
                    EntityNoteInfo noteInfo;
                   noteInfo = noteList.get(count);
                    roomDao.insertNote(noteInfo);
                }



    }

    private void insertCourse(String courseId, String title) {
        if (courseInfo != null)
            courseInfo = null;
        courseInfo = new EntityCourseInfo();
        courseInfo.setCourseId(courseId);
        courseInfo.setCourseTitle(title);
        courseList.add(courseInfo);
    }


    private void insertNote(String courseId, String title, String text) {


        if (noteInfo != null)
            noteInfo = null;
        noteInfo = new EntityNoteInfo();
        noteInfo.setCourseId(courseId);
        noteInfo.setNoteTitle(title);
        noteInfo.setNoteText(text);
        noteList.add(noteInfo);

    }


}
