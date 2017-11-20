package com.inclass.mad.inclassassgn03;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

/*
In Class Assignment 03
Atul Kumar Banwar
Sanket Patil
File Name: MainActivity.java
*/

public class MainActivity extends Activity {

    private EditText edtTxtName;
    private EditText edtTxtEmail;
    private RadioGroup rdGrpDepartment;
    private SeekBar skBarCurrentMoodValue;

    final static String STD_OBJ_KEY = "STUDENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTxtName = (EditText) findViewById(R.id.edtTxtName);
        edtTxtEmail = (EditText) findViewById(R.id.edtTxtEmail);
        rdGrpDepartment = (RadioGroup) findViewById(R.id.rdGrpDepartment);
        skBarCurrentMoodValue = (SeekBar) findViewById(R.id.skBarCurrentMoodValue);
    }

    /**
     * Submit Button handler
     *
     * @param view
     */
    public void submitStudentInfo(View view) {
        String name = "";
        String email = "";
        String department = "";
        int mood = 0;

        name = edtTxtName.getText().toString();
        email = edtTxtEmail.getText().toString();

        if (rdGrpDepartment.getCheckedRadioButtonId() == R.id.rdBtnSIS) {
            department = getResources().getString(R.string.lblRdBtnSIS);
        } else if (rdGrpDepartment.getCheckedRadioButtonId() == R.id.rdBtnCS) {
            department = getResources().getString(R.string.lblRdBtnCS);
        } else if (rdGrpDepartment.getCheckedRadioButtonId() == R.id.rdBtnBIO) {
            department = getResources().getString(R.string.lblRdBtnBIO);
        } else if (rdGrpDepartment.getCheckedRadioButtonId() == R.id.rdBtnOthers) {
            department = getResources().getString(R.string.lblRdBtnOthers);
        }

        mood = skBarCurrentMoodValue.getProgress();

        if (name.length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errMsgEmptyName), Toast.LENGTH_SHORT).show();
        } else if (email.length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errMsgEmptyEmail), Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errInvalidEmail), Toast.LENGTH_SHORT).show();
        } else {
            Student stdObj = new Student(name, email, department, mood);

            Intent intentObj = new Intent(getBaseContext(), DisplayActivity.class);
            intentObj.putExtra(STD_OBJ_KEY, stdObj);
            startActivity(intentObj);
        }
    }

    /**
     * Email Address Validator
     *
     * @param emailString
     * @return
     */
    public final static boolean isValidEmail(CharSequence emailString) {
        if (emailString == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
        }
    }
}
