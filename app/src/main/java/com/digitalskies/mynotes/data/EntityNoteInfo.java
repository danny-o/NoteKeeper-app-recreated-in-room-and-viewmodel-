package com.digitalskies.mynotes.data;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "NoteInfo")
public class EntityNoteInfo {

   @PrimaryKey(autoGenerate = true)
   @ColumnInfo(name="id")
   private Long noteId;

   @NonNull
   @ColumnInfo(name="courseId")
   private String courseId;

   @ColumnInfo(name="noteTitle")
   private String noteTitle;

   @ColumnInfo(name="noteText")
   private String noteText;

   @NonNull
   public String getCourseId() {
      return courseId;
   }
   public Long getNoteId() {
      return noteId;
   }

   public void setNoteId(Long noteId) {
      this.noteId = noteId;
   }

   public void setCourseId(@NonNull String courseId) {
      this.courseId = courseId;
   }

   public String getNoteTitle() {
      return noteTitle;
   }

   public void setNoteTitle(String noteTitle) {
      this.noteTitle = noteTitle;
   }

   public String getNoteText() {
      return noteText;
   }

   public void setNoteText(String noteText) {
      this.noteText = noteText;
   }
}
