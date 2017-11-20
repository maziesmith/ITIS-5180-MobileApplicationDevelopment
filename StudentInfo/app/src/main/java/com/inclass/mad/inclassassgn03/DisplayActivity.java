package com.inclass.mad.inclassassgn03;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/*
In Class Assignment 03
Atul Kumar Banwar
Sanket Patil
File Name: DisplayActivity.java
*/

public class DisplayActivity extends Activity {
    private TextView txtViewNameValue;
    private TextView txtViewEmailValue;
    private TextView txtViewDepartmentValue;
    private TextView txtViewMood;

    private Student stdObj = null;
    private Intent intentObj = null;

    public final static String NAME_KEY = "NAME";
    public final static String EMAIL_KEY = "EMAIL";
    public final static String DEPARTMENT_KEY = "DEPARTMENT";
    public final static String MOOD_KEY = "MOOD";
    public final static String VALUE_KEY = "VALUE";

    public static final int REQ_CODE_NAME = 1;
    public static final int REQ_CODE_EMAIL = 2;
    public static final int REQ_CODE_DEPARTMENT = 3;
    public static final int REQ_CODE_MOOD = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        txtViewNameValue = (TextView) findViewById(R.id.txtViewNameValue);
        txtViewEmailValue = (TextView) findViewById(R.id.txtViewEmailValue);
        txtViewDepartmentValue = (TextView) findViewById(R.id.txtViewDepartmentValue);
        txtViewMood = (TextView) findViewById(R.id.txtViewMoodValue);

        if (getIntent().getExtras() != null) {
            stdObj = getIntent().getExtras().getParcelable(MainActivity.STD_OBJ_KEY);
        }

        if (stdObj != null) {
            txtViewNameValue.setText(stdObj.getName());
            txtViewEmailValue.setText(stdObj.getEmail());
            txtViewDepartmentValue.setText(stdObj.getDepartment());
            txtViewMood.setText(stdObj.getMood() + " % Positive");
        }
    }

    /**
     * Edit Name Handler
     *
     * @param view
     */
    public void editName(View view) {
        intentObj = new Intent(DisplayActivity.this, EditActivity.class);
        intentObj.putExtra(NAME_KEY, stdObj.getName());
        intentObj.setAction(NAME_KEY);
        startActivityForResult(intentObj, REQ_CODE_NAME);
    }

    /**
     * Edit Email Handler
     *
     * @param view
     */
    public void editEmail(View view) {
        intentObj = new Intent(DisplayActivity.this, EditActivity.class);
        intentObj.putExtra(EMAIL_KEY, stdObj.getEmail());
        intentObj.setAction(EMAIL_KEY);
        startActivityForResult(intentObj, REQ_CODE_EMAIL);
    }

    /**
     * Edit Department Handler
     *
     * @param view
     */
    public void editDepartment(View view) {
        intentObj = new Intent(DisplayActivity.this, EditActivity.class);
        intentObj.putExtra(DEPARTMENT_KEY, stdObj.getDepartment());
        intentObj.setAction(DEPARTMENT_KEY);
        startActivityForResult(intentObj, REQ_CODE_DEPARTMENT);
    }

    /**
     * Edit Mood Handler
     *
     * @param view
     */
    public void editMood(View view) {
        intentObj = new Intent(DisplayActivity.this, EditActivity.class);
        intentObj.putExtra(MOOD_KEY, stdObj.getMood());
        intentObj.setAction(MOOD_KEY);
        startActivityForResult(intentObj, REQ_CODE_MOOD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String value = "";

        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_NAME) {
                value = data.getExtras().getString(VALUE_KEY);
                stdObj.setName(value);
                txtViewNameValue.setText(value);
            } else if (requestCode == REQ_CODE_EMAIL) {
                value = data.getExtras().getString(VALUE_KEY);
                stdObj.setEmail(value);
                txtViewEmailValue.setText(value);
            } else if (requestCode == REQ_CODE_DEPARTMENT) {
                value = data.getExtras().getString(VALUE_KEY);
                stdObj.setDepartment(value);
                txtViewDepartmentValue.setText(value);
            } else if (requestCode == REQ_CODE_MOOD) {
                int moodValue = data.getExtras().getInt(VALUE_KEY);
                stdObj.setMood(moodValue);
                txtViewMood.setText(moodValue + " % Positive");
            }
        } else if (resultCode == RESULT_CANCELED) {
            //DO Nothing
        }
    }
}
