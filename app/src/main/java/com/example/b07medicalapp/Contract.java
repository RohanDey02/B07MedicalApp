package com.example.b07medicalapp;

import android.content.Context;

public interface Contract {

    public interface Model{
        public void queryDoctor(Context context, String user_id, String user_pass);
        public void queryPatient(Context context, String user_id, String user_pass, android.view.View view);
    }

    public interface View{
        public void login(android.view.View view);
    }

    public interface Presenter{
        public void success(Context context);
        public void fail(android.view.View view);
    }
}
