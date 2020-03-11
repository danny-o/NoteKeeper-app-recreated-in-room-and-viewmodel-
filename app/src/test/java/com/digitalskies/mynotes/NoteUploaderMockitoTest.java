package com.digitalskies.mynotes;


import android.content.Context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoteUploaderMockitoTest {


    @Mock
    Context context;

    ArrayList<String> arrayList=new ArrayList<>();



   NoteUploader uploader=new NoteUploader(context);



    @Test
    public void testUploader() {
        uploader.simulateLongRunningWork(5);
        int r = uploader.simulateLongRunningWork(5);
       assertEquals(25,r);
        String gg=uploader.nn.get(0);
        assertEquals(gg,"name");

    }



}