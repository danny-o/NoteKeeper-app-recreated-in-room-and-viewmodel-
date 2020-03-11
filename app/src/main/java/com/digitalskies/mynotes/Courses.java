package com.digitalskies.mynotes;

import com.digitalskies.mynotes.data.EntityCourseInfo;

import java.util.ArrayList;
import java.util.List;

public class Courses {

    private List<EntityCourseInfo> courseList;
    private List<String> courseTitles=new ArrayList<>();
    private String courseTitle="";
    private Integer index=0;

    public Courses() {
        courseList=new ArrayList<>();
    }



    public int getIndexToSet(String courseId) {
        if(courseId.equals("")){
            index=0;
                   }
       else{
           for(int count=0;count<courseList.size();count++){
               if(courseId.equals(courseList.get(count).getCourseId())){
                   index = count;
               }
           }
        }
       return index;
    }
    public List<String> getCourseTitles(List<EntityCourseInfo> list){
            courseList=list;
            for(int count=0;count<courseList.size();count++){
               courseTitles.add(courseList.get(count).getCourseTitle());
            }
            return  courseTitles;
    }

    public String getCourseId(int itemSelectedPosition){
        return courseList.get(itemSelectedPosition).getCourseId();
    }
}
