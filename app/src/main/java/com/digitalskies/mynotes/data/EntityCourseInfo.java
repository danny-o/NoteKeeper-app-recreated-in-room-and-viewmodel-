package com.digitalskies.mynotes.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CourseInfo")
public class EntityCourseInfo {

    @ColumnInfo(name="CourseTitle")
    private String CourseTitle;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name="CourseId")
    private String CourseId;

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }

    public String getCourseTitle() {
        return CourseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        CourseTitle = courseTitle;
    }
}
