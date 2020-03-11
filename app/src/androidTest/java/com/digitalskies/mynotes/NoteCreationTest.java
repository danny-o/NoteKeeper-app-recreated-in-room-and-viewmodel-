package com.digitalskies.mynotes;

import android.view.Menu;

import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.*;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {
    //the activity test rule creates instance of activity and destroys the activity.
    //the test rule needs to be public
    @Rule
    public ActivityTestRule<NoteListActivity> mNoteActivityListRule=new ActivityTestRule<>(NoteListActivity.class);
    static DataManager sDataManager;
    @BeforeClass
    public static void classSetUp()throws Exception{
        sDataManager=DataManager.getInstance();
    }

    @Test
    public void createNewNote(){
        final CourseInfo course=sDataManager.getCourse("java_lang");
        final String noteTitle="Test note Title";
        final String noteText="This is the body of our test note";
        //The onView method returns a reference to a ViewInteraction Object
        //The onView method is accepting Hamcrest matchers in this case ViewMatchers method 'withId' which returns Hamcrest matchers
        //ViewInteraction viewInteraction=onView(withId(R.id.fab));
        //The perform method accepts methods specifying the action to be performed as parameters
        //These methods are provided by ViewActions class
        //viewInteraction.perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.spinner_courses)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class),equalTo(course))).perform(click());
        //confirming expected UI behavior
        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(containsString(course.getTitle()))));
        onView(withId(R.id.text_note_title)).perform(typeText(noteTitle))
                .check(matches(withText(containsString(noteTitle))));
        onView(withId(R.id.text_note_text_text)).perform(typeText(noteText),
                closeSoftKeyboard());
        onView(withId(R.id.text_note_text_text)).check(matches(withText(containsString(noteText))));



        pressBack();
        int noteIndex=sDataManager.getNotes().size()-1;
        NoteInfo note=sDataManager.getNotes().get(noteIndex);
        //confirming expected logic behavior
        assertEquals(course,note.getCourse());
        assertEquals(noteTitle,note.getTitle());
        assertEquals(noteText,note.getText());
    }



}
