package com.example.b07medicalapp;

import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    private Presenter presenter;

    @Mock
    private Model model;

    @Mock
    private MainActivity view;

    @Mock
    View v;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        presenter = Mockito.spy(new Presenter(view, model));
    }


    @Test
    public void presenterGetData() throws Exception {
        String username = "patient1";
        String password = "pA0001";

        Mockito.doNothing().when(model).queryDoctor(view, username, password, presenter, v);
        Mockito.doNothing().when(model).queryPatient(v, username, password, view, presenter);
        presenter.getData(v, username, password);

        Mockito.verify(model, Mockito.times(1)).queryPatient(v, username, password, view, presenter);
    }

    @Test
    public void presenterGetIncorrectData() throws Exception {
        String username = "patient1";
        String password = "pA0002";

        Mockito.doNothing().when(model).queryDoctor(view, username, password, presenter, v);
        Mockito.doNothing().when(model).queryPatient(v, username, password, view, presenter);
        presenter.getData(v, username, password);

        Mockito.verify(model, Mockito.times(1)).queryPatient(v, username, password, view, presenter);
    }
    @Test
    public void presenterDetermineSuccess() throws Exception {
        String username = "patient1";
        String password = "pA0001";

        Mockito.doNothing().when(model).queryDoctor(view, username, password, presenter, v);
        Mockito.doNothing().when(model).queryPatient(v, username, password, view, presenter);
        presenter.getData(v, username, password);
        presenter.determiner(true, v);

        Mockito.verify(view, Mockito.times(1)).success();
    }
    @Test
    public void presenterDetermineFailure() throws Exception {
        String username = "patient1";
        String password = "pA0002";

        Mockito.doNothing().when(model).queryDoctor(view, username, password, presenter, v);
        Mockito.doNothing().when(model).queryPatient(v, username, password, view, presenter);
        presenter.getData(v, username, password);
        presenter.determiner(false, v);

        Mockito.verify(view, Mockito.times(1)).fail(v);
    }



}