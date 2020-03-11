package com.digitalskies.mynotes.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomDao {

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insertNote(EntityNoteInfo note);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourseInfo(EntityCourseInfo course);

    @Query("SELECT CourseInfo.CourseTitle AS CourseTitle,NoteTitle AS NoteTitle,NoteInfo.id AS id FROM CourseInfo"+
            " INNER JOIN NoteInfo ON NoteInfo.courseId=CourseInfo.CourseId")
    LiveData<List<NoteDetails>> getNotesDetails();


    @Query("SELECT * FROM NoteInfo WHERE NoteInfo.noteTitle= :noteTitle")
    EntityNoteInfo getNoteByNoteTitle(String noteTitle);

    @Query("SELECT * FROM NoteInfo WHERE NoteInfo.id= :id")
    EntityNoteInfo getNoteById(Long id);

    @Query("SELECT * FROM CourseInfo")
    List<EntityCourseInfo> getCourses();

    @Update
    void updateNote(EntityNoteInfo note);

    @Delete
    void deleteNote(EntityNoteInfo note);



}
