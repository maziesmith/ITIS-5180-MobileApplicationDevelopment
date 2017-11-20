package com.inclass.mad.inclassassgn03;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/*
In Class Assignment 03
Atul Kumar Banwar
Sanket Patil
File Name: EditActivity.java
*/

public class EditActivity extends Activity {

    private EditText edtTxtName;
    private EditText edtTxtEmail;
    private TextView txtViewDepartment;
    private RadioGroup rdGrpDepartment;
    private RadioButton rdBtnSIS;
    private RadioButton rdBtnCS;
    private RadioButton rdBtnBIO;
    private RadioButton rdBtnOthers;
    private TextView txtViewCurrentMood;
    private SeekBar skBarCurrentMoodValue;

    private String name = null;
    private String email = null;
    private String department = null;
    private int mood = 0;
    private String requestAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edtTxtName = (EditText) findViewById(R.id.edtTxtName);
        edtTxtEmail = (EditText) findViewById(R.id.edtTxtEmail);
        txtViewDepartment = (TextView) findViewById(R.id.txtViewDepartment);
        rdGrpDepartment = (RadioGroup) findViewById(R.id.rdGrpDepartment);
        rdBtnSIS = (RadioButton) findViewById(R.id.rdBtnSIS);
        rdBtnCS = (RadioButton) findViewById(R.id.rdBtnCS);
        rdBtnBIO = (RadioButton) findViewById(R.id.rdBtnBIO);
        rdBtnOthers = (RadioButton) findViewById(R.id.rdBtnOthers);
        txtViewCurrentMood = (TextView) findViewById(R.id.txtViewCurrentMood);
        skBarCurrentMoodValue = (SeekBar) findViewById(R.id.skBarCurrentMoodValue);

        if (getIntent().getExtras() != null) {
            requestAction = getIntent().getAction();

            if (requestAction == DisplayActivity.NAME_KEY) {

                name = getIntent().getExtras().getString(DisplayActivity.NAME_KEY);
                edtTxtName.setVisibility(View.VISIBLE);
                edtTxtName.setText(name);
                edtTxtEmail.setVisibility(View.INVISIBLE);
                txtViewDepartment.setVisibility(View.INVISIBLE);
                rdGrpDepartment.setVisibility(View.INVISIBLE);
                txtViewCurrentMood.setVisibility(View.INVISIBLE);
                skBarCurrentMoodValue.setVisibility(View.INVISIBLE);

            } else if (requestAction == DisplayActivity.EMAIL_KEY) {

                email = getIntent().getExtras().getString(DisplayActivity.EMAIL_KEY);
                edtTxtName.setVisibility(View.INVISIBLE);
                edtTxtEmail.setVisibility(View.VISIBLE);
                edtTxtEmail.setText(email);
                txtViewDepartment.setVisibility(View.INVISIBLE);
                rdGrpDepartment.setVisibility(View.INVISIBLE);
                txtViewCurrentMood.setVisibility(View.INVISIBLE);
                skBarCurrentMoodValue.setVisibility(View.INVISIBLE);

            } else if (requestAction == DisplayActivity.DEPARTMENT_KEY) {

                department = getIntent().getExtras().getString(DisplayActivity.DEPARTMENT_KEY);
                edtTxtName.setVisibility(View.INVISIBLE);
                edtTxtEmail.setVisibility(View.INVISIBLE);
                rdGrpDepartment.setVisibility(View.VISIBLE);
                if (department.equalsIgnoreCase(getResources().getString(R.string.lblRdBtnSIS).toString())) {
                    rdBtnSIS.setChecked(true);
                } else if (department.equalsIgnoreCase(getResources().getString(R.string.lblRdBtnCS).toString())) {
                    rdBtnCS.setChecked(true);
                } else if (department.equalsIgnoreCase(getResources().getString(R.string.lblRdBtnBIO).toString())) {
                    rdBtnBIO.setChecked(true);
                } else {
                    rdBtnOthers.setChecked(true);
                }
                txtViewCurrentMood.setVisibility(View.INVISIBLE);
                skBarCurrentMoodValue.setVisibility(View.INVISIBLE);
            } else if (requestAction == DisplayActivity.MOOD_KEY) {

                mood = getIntent().getExtras().getInt(DisplayActivity.MOOD_KEY);
                edtTxtName.setVisibility(View.INVISIBLE);
                edtTxtEmail.setVisibility(View.INVISIBLE);
                txtViewDepartment.setVisibility(View.INVISIBLE);
                rdGrpDepartment.setVisibility(View.INVISIBLE);
                txtViewCurrentMood.setVisibility(View.VISIBLE);
                skBarCurrentMoodValue.setVisibility(View.VISIBLE);
                skBarCurrentMoodValue.setProgress(mood);

            }

        }
    }

    /**
     * Save Button Handler
     * @param view
     */
    public void saveInfo(View view) {

        if (requestAction == DisplayActivity.NAME_KEY) {

            name = edtTxtName.getText().toString();
            if (name.length() != 0) {
                Intent intent = new Intent();
                intent.putExtra(DisplayActivity.VALUE_KEY, name);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.errMsgEmptyName), Toast.LENGTH_SHORT).show();
            }
        } else if (requestAction == DisplayActivity.EMAIL_KEY) {

            email = edtTxtEmail.getText().toString();
            if (email.length() == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.errMsgEmptyEmail), Toast.LENGTH_SHORT).show();
            } else if (!MainActivity.isValidEmail(email)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.errInvalidEmail), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra(DisplayActivity.VALUE_KEY, email);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (requestAction == DisplayActivity.DEPARTMENT_KEY) {

            if (rdBtnSIS.isChecked()) {
                department = getResources().getString(R.string.lblRdBtnSIS);
            } else if (rdBtnCS.isChecked()) {
                department = getResources().getString(R.string.lblRdBtnCS);
            } else if (rdBtnBIO.isChecked()) {
                department = getResources().getString(R.string.lblRdBtnBIO);
            } else {
                department = getResources().getString(R.string.lblRdBtnOthers);
            }

            if (department != null | department.length() != 0) {
                Intent intent = new Intent();
                intent.putExtra(DisplayActivity.VALUE_KEY, department);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                setResult(RESULT_CANCELED);
            }
        } else if (requestAction == DisplayActivity.MOOD_KEY) {
            mood = skBarCurrentMoodValue.getProgress();

            Intent intent = new Intent();
            intent.putExtra(DisplayActivity.VALUE_KEY, mood);
            setResult(RESULT_OK, intent);
            finish();
        }

    }
}
