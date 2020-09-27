package com.example.lerron.devtimeacademico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class Academico extends AppCompatActivity {

    private Toolbar mToolbar;
    private Toolbar mToolbarBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academico);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("DevTime Sistemas");
        mToolbar.setSubtitle("BI Acadêmico");
        mToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(mToolbar);
    }
}
